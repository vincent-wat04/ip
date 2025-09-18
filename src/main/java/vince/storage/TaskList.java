package vince.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import vince.task.Task;
import vince.task.Todo;
import vince.task.Deadline;
import vince.task.Event;
import vince.task.TaskType;
import vince.util.DateTimeParser;
import vince.exception.VinceException;

/**
 * In-memory collection of tasks that also integrates persistence via
 * {@link Storage}.
 * Provides operations to add, list, query-by-date, and mutate tasks
 * (mark/unmark/delete).
 */
public class TaskList {
    // Constants for task parsing
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";
    private static final String EVENT_SEPARATOR_PATTERN = " /from | /to ";
    
    private final Storage storage;
    private ArrayList<Task> tasks = new ArrayList<Task>();

    /**
     * Constructs a task list, loading existing tasks from the default storage
     * location if present.
     */
    public TaskList() {
        this.storage = new Storage();
        this.tasks = storage.load();
    }

    /**
     * Parses the raw user input and adds the corresponding task.
     * Supports todo, deadline (/by) and event (/from ... /to ...) formats.
     * 
     * @param input raw input line
     * @return the newly added task
     * @throws VinceException if the input format is invalid
     */
    public Task addTask(String input) throws VinceException {
        String commandType = extractCommandType(input);
        TaskType taskType = TaskType.fromCommand(commandType);
        
        if (taskType == null) {
            return addGenericTask(input);
        }
        
        Task newTask = createTaskByType(input, taskType);
        tasks.add(newTask);
        storage.save(tasks);
        return newTask;
    }

    /**
     * Extracts the command type from the input string.
     * 
     * @param input the raw input string
     * @return the first word which represents the command type
     */
    private String extractCommandType(String input) {
        return input.split(" ")[0];
    }

    /**
     * Adds a generic task when no specific task type is recognized.
     * 
     * @param input the raw input string
     * @return the newly created generic task
     */
    private Task addGenericTask(String input) {
        Task newTask = new Task(input);
        tasks.add(newTask);
        storage.save(tasks);
        return newTask;
    }

    /**
     * Creates a task based on the specified task type.
     * 
     * @param input the raw input string
     * @param taskType the type of task to create
     * @return the newly created task
     * @throws VinceException if the input format is invalid for the task type
     */
    private Task createTaskByType(String input, TaskType taskType) throws VinceException {
        switch (taskType) {
            case DEADLINE:
                return createDeadlineTask(input, taskType);
            case EVENT:
                return createEventTask(input, taskType);
            case TODO:
                return createTodoTask(input, taskType);
            default:
                throw new VinceException("Unknown task type: " + taskType);
        }
    }

    /**
     * Creates a deadline task from the input string.
     * 
     * @param input the raw input string
     * @param taskType the task type (should be DEADLINE)
     * @return the newly created deadline task
     * @throws VinceException if the input format is invalid
     */
    private Task createDeadlineTask(String input, TaskType taskType) throws VinceException {
        if (!input.contains(DEADLINE_SEPARATOR)) {
            throw new VinceException("Deadline task must contain ' /by ' to specify the deadline!");
        }
        
        String[] deadlineParts = input.split(DEADLINE_SEPARATOR);
        String description = deadlineParts[0].substring(taskType.getPrefixLength());
        String deadline = deadlineParts[1];
        
        return new Deadline(description, deadline);
    }

    /**
     * Creates an event task from the input string.
     * 
     * @param input the raw input string
     * @param taskType the task type (should be EVENT)
     * @return the newly created event task
     * @throws VinceException if the input format is invalid
     */
    private Task createEventTask(String input, TaskType taskType) throws VinceException {
        if (!input.contains(EVENT_FROM_SEPARATOR) || !input.contains(EVENT_TO_SEPARATOR)) {
            throw new VinceException("Event task must contain ' /from ' and ' /to ' to specify the event time!");
        }
        
        String[] eventParts = input.split(EVENT_SEPARATOR_PATTERN);
        String description = eventParts[0].substring(taskType.getPrefixLength());
        String startTime = eventParts[1];
        String endTime = eventParts[2];
        
        return new Event(description, startTime, endTime);
    }

    /**
     * Creates a todo task from the input string.
     * 
     * @param input the raw input string
     * @param taskType the task type (should be TODO)
     * @return the newly created todo task
     * @throws VinceException if the input format is invalid
     */
    private Task createTodoTask(String input, TaskType taskType) throws VinceException {
        if (input.length() < taskType.getPrefixLength()) {
            throw new VinceException("Todo task must start with 'todo ' and have a description!");
        }
        
        String description = input.substring(taskType.getPrefixLength());
        return new Todo(description);
    }

    /**
     * Returns the number of tasks currently stored.
     * 
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Builds preformatted numbered lines for all tasks in this list.
     * Uses Java Streams for functional programming approach.
     * 
     * @return list of lines ready for display
     */
    public List<String> list() {
        return IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.toList());
    }

    /**
     * Builds preformatted numbered lines for tasks whose description contains the
     * keyword.
     * Matching is case-insensitive and ignores leading/trailing spaces in keyword.
     * Uses Java Streams for functional programming approach.
     * 
     * @param keyword search keyword
     * @return matching task lines
     */
    public List<String> findTasks(String keyword) {
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        if (key.isEmpty()) {
            return new ArrayList<>();
        }
        
        return IntStream.range(0, tasks.size())
                .filter(i -> tasks.get(i).getDescription().toLowerCase().contains(key))
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.toList());
    }

    /**
     * Builds preformatted numbered lines for tasks that occur on the given date.
     * Deadlines are matched by their date; events by spanning the date range.
     * Uses Java Streams for functional programming approach.
     * 
     * @param dateStr date string accepted by {@link DateTimeParser}
     * @return lines for tasks matching that date
     * @throws VinceException if the date string is invalid
     */
    public List<String> tasksOnDateLines(String dateStr) throws VinceException {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        
        return IntStream.range(0, tasks.size())
                .filter(i -> isTaskOnDate(tasks.get(i), targetDate))
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a task occurs on the given date.
     * Deadlines match if their date equals the target date.
     * Events match if the target date falls within their date range.
     * 
     * @param task the task to check
     * @param targetDate the date to match against
     * @return true if the task occurs on the target date
     */
    private boolean isTaskOnDate(Task task, LocalDate targetDate) {
        if (task instanceof Deadline) {
            LocalDateTime taskDate = ((Deadline) task).getBy();
            return taskDate.toLocalDate().equals(targetDate);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            LocalDate eventStartDate = event.getFrom().toLocalDate();
            LocalDate eventEndDate = event.getTo().toLocalDate();
            return !targetDate.isBefore(eventStartDate) && !targetDate.isAfter(eventEndDate);
        }
        return false;
    }

    /**
     * Formats the target date label for display.
     * 
     * @param dateStr date string accepted by {@link DateTimeParser}
     * @return formatted label such as "Dec 15 2024"
     */
    public String tasksOnDateLabel(String dateStr) {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        return DateTimeParser.formatDate(targetDate.atStartOfDay());
    }

    /**
     * Generates a schedule view for tasks on a specific date.
     * Tasks are organized by time with a timeline format.
     * 
     * @param dateStr date string accepted by {@link DateTimeParser}
     * @return formatted schedule lines with timeline
     * @throws VinceException if the date string is invalid
     */
    public List<String> getScheduleForDate(String dateStr) throws VinceException {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        
        // Get all tasks that occur on this date
        List<TaskWithTime> tasksWithTime = IntStream.range(0, tasks.size())
                .filter(i -> isTaskOnDate(tasks.get(i), targetDate))
                .mapToObj(i -> new TaskWithTime(tasks.get(i), i + 1, targetDate))
                .sorted(Comparator.comparing(TaskWithTime::getTime))
                .collect(Collectors.toList());
        
        if (tasksWithTime.isEmpty()) {
            return List.of("No tasks scheduled for this date.");
        }
        
        List<String> scheduleLines = new ArrayList<>();
        scheduleLines.add("📅 Daily Schedule:");
        scheduleLines.add("");
        
        LocalTime currentTime = LocalTime.MIDNIGHT;
        boolean hasAllDayTasks = false;
        
        // First, add all-day tasks
        for (TaskWithTime taskWithTime : tasksWithTime) {
            if (taskWithTime.isAllDay()) {
                if (!hasAllDayTasks) {
                    scheduleLines.add("🌅 All Day:");
                    hasAllDayTasks = true;
                }
                scheduleLines.add("  • " + taskWithTime.getTask().toString());
            }
        }
        
        // Then add timed tasks
        for (TaskWithTime taskWithTime : tasksWithTime) {
            if (!taskWithTime.isAllDay()) {
                LocalTime taskTime = taskWithTime.getTime();
                
                // Add time gap if there's a significant gap
                if (currentTime != LocalTime.MIDNIGHT && 
                    taskTime.isAfter(currentTime.plusHours(1))) {
                    scheduleLines.add("");
                }
                
                // Add time header
                String timeHeader = String.format("🕐 %s:", 
                    taskTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                scheduleLines.add(timeHeader);
                
                // Add task details
                String taskDetails = "  • " + taskWithTime.getTask().toString();
                if (taskWithTime.getTask() instanceof Event) {
                    Event event = (Event) taskWithTime.getTask();
                    LocalTime endTime = event.getTo().toLocalTime();
                    taskDetails += String.format(" (until %s)", 
                        endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                }
                scheduleLines.add(taskDetails);
                
                currentTime = taskTime;
            }
        }
        
        return scheduleLines;
    }

    /**
     * Helper class to represent a task with its associated time for scheduling.
     */
    private static class TaskWithTime {
        private final Task task;
        private final int index;
        private final LocalTime time;
        private final boolean isAllDay;

        public TaskWithTime(Task task, int index, LocalDate targetDate) {
            this.task = task;
            this.index = index;
            
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                this.time = deadline.getBy().toLocalTime();
                this.isAllDay = this.time.equals(LocalTime.MIDNIGHT);
            } else if (task instanceof Event) {
                Event event = (Event) task;
                this.time = event.getFrom().toLocalTime();
                this.isAllDay = this.time.equals(LocalTime.MIDNIGHT) && 
                               event.getTo().toLocalTime().equals(LocalTime.MIDNIGHT);
            } else {
                // Todo tasks are treated as all-day
                this.time = LocalTime.MIDNIGHT;
                this.isAllDay = true;
            }
        }

        public Task getTask() {
            return task;
        }

        public int getIndex() {
            return index;
        }

        public LocalTime getTime() {
            return time;
        }

        public boolean isAllDay() {
            return isAllDay;
        }
    }

    /**
     * Retrieves a task by 1-based index string.
     * 
     * @param index 1-based index string (e.g., "1")
     * @return the task at that index
     * @throws VinceException if index is invalid or out of bounds
     */
    public Task get(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException(
                    "Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        Task result = tasks.get(taskIndex);
        assert result != null : "Task at valid index should not be null";
        return result;
    }

    /**
     * Marks a task as done.
     * 
     * @param index 1-based index of the task
     * @throws VinceException if index is invalid or out of bounds
     */
    public void mark(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException(
                    "Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        Task task = tasks.get(taskIndex);
        assert task != null : "Task at valid index should not be null";
        assert !task.isDone() : "Task should not already be marked as done";
        task.mark();
        assert task.isDone() : "Task should be marked as done after mark() call";
        storage.save(tasks);
    }

    /**
     * Unmarks a task (set as not done).
     * 
     * @param index 1-based index of the task
     * @throws VinceException if index is invalid or out of bounds
     */
    public void unmark(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException(
                    "Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        Task task = tasks.get(taskIndex);
        assert task != null : "Task at valid index should not be null";
        assert task.isDone() : "Task should be marked as done before unmarking";
        task.unmark();
        assert !task.isDone() : "Task should not be marked as done after unmark() call";
        storage.save(tasks);
    }

    /**
     * Deletes a task by 1-based index.
     * 
     * @param index 1-based index string of the task to remove
     * @return the removed task
     * @throws VinceException if index is invalid or out of bounds
     */
    public Task delete(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException(
                    "Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        int originalSize = tasks.size();
        Task removed = tasks.remove(taskIndex);
        assert removed != null : "Removed task should not be null";
        assert tasks.size() == originalSize - 1 : "Task list size should decrease by 1 after deletion";
        storage.save(tasks);
        return removed;
    }

    public void deleteAll() {
        // first remove all tasks stored in local variable
        tasks.clear();
        storage.save(tasks);
    }
    
    /**
     * Returns all tasks for AI analysis.
     * @return list of all tasks
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
