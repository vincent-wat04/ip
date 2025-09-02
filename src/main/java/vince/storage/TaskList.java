package vince.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import vince.task.Task;
import vince.task.Todo;
import vince.task.Deadline;
import vince.task.Event;
import vince.task.TaskType;
import vince.util.DateTimeParser;
import vince.exception.VinceException;

/**
 * In-memory collection of tasks that also integrates persistence via {@link Storage}.
 * Provides operations to add, list, query-by-date, and mutate tasks (mark/unmark/delete).
 */
public class TaskList {
    private final Storage storage;
    private ArrayList<Task> tasks = new ArrayList<Task>();

    /**
     * Constructs a task list, loading existing tasks from the default storage location if present.
     */
    public TaskList() {
        this.storage = new Storage();
        this.tasks = storage.load();
    }

    /**
     * Parses the raw user input and adds the corresponding task.
     * Supports todo, deadline (/by) and event (/from ... /to ...) formats.
     * @param input raw input line
     * @return the newly added task
     * @throws VinceException if the input format is invalid
     */
    public Task addTask(String input) throws VinceException {
        String type = input.split(" ")[0];
        TaskType taskType = TaskType.fromCommand(type);
        if (taskType == null) {
            tasks.add(new Task(input));
            storage.save(tasks);
            return tasks.get(tasks.size() - 1);
        }
        switch (taskType) {
            case DEADLINE:
                if (!input.contains(" /by ")) {
                    throw new VinceException("Deadline task must contain ' /by ' to specify the deadline!");
                }
                String[] deadlineParts = input.split(" /by ");
                tasks.add(new Deadline(deadlineParts[0].substring(taskType.getPrefixLength()), deadlineParts[1]));
                break;
            case EVENT:
                if (!input.contains(" /from ") || !input.contains(" /to ")) {
                    throw new VinceException("Event task must contain ' /from ' and ' /to ' to specify the event time!");
                }
                String[] eventParts = input.split(" /from | /to ");
                tasks.add(new Event(eventParts[0].substring(taskType.getPrefixLength()), eventParts[1], eventParts[2]));
                break;
            case TODO:
                if (input.length() < taskType.getPrefixLength()) {
                    throw new VinceException("Todo task must start with 'todo ' and have a description!");
                }
                tasks.add(new Todo(input.substring(taskType.getPrefixLength())));
                break;
        }
        storage.save(tasks);
        return tasks.get(tasks.size() - 1);
    }

    /**
     * Returns the number of tasks currently stored.
     * @return task count
     */
    public int size() { return tasks.size(); }

    /**
     * Builds preformatted numbered lines for all tasks in this list.
     * @return list of lines ready for display
     */
    public List<String> list() {
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add((i + 1) + ". " + tasks.get(i));
        }
        return lines;
    }

    /**
     * Builds preformatted numbered lines for tasks that occur on the given date.
     * Deadlines are matched by their date; events by spanning the date range.
     * @param dateStr date string accepted by {@link DateTimeParser}
     * @return lines for tasks matching that date
     * @throws VinceException if the date string is invalid
     */
    public List<String> tasksOnDateLines(String dateStr) throws VinceException {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task instanceof Deadline) {
                LocalDateTime taskDate = ((Deadline) task).getBy();
                if (taskDate.toLocalDate().equals(targetDate)) {
                    lines.add((i + 1) + ". " + task);
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate eventStartDate = event.getFrom().toLocalDate();
                LocalDate eventEndDate = event.getTo().toLocalDate();
                if (!targetDate.isBefore(eventStartDate) && !targetDate.isAfter(eventEndDate)) {
                    lines.add((i + 1) + ". " + task);
                }
            }
        }
        return lines;
    }

    /**
     * Formats the target date label for display.
     * @param dateStr date string accepted by {@link DateTimeParser}
     * @return formatted label such as "Dec 15 2024"
     */
    public String tasksOnDateLabel(String dateStr) {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        return DateTimeParser.formatDate(targetDate.atStartOfDay());
    }

    /**
     * Retrieves a task by 1-based index string.
     * @param index 1-based index string (e.g., "1")
     * @return the task at that index
     * @throws VinceException if index is invalid or out of bounds
     */
    public Task get(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        return tasks.get(taskIndex);
    }

    /**
     * Marks a task as done.
     * @param index 1-based index of the task
     * @throws VinceException if index is invalid or out of bounds
     */
    public void mark(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.get(taskIndex).mark();
        storage.save(tasks);
    }

    /**
     * Unmarks a task (set as not done).
     * @param index 1-based index of the task
     * @throws VinceException if index is invalid or out of bounds
     */
    public void unmark(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.get(taskIndex).unmark();
        storage.save(tasks);
    }

    /**
     * Deletes a task by 1-based index.
     * @param index 1-based index string of the task to remove
     * @return the removed task
     * @throws VinceException if index is invalid or out of bounds
     */
    public Task delete(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        Task removed = tasks.remove(taskIndex);
        storage.save(tasks);
        return removed;
    }

    public void deleteAll() {
        // first remove all tasks stored in local variable
        tasks.clear();
        storage.save(tasks);
    }
}
