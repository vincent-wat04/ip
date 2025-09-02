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

public class TaskList {
    private final Storage storage;
    private ArrayList<Task> tasks = new ArrayList<Task>();

    public TaskList() {
        this.storage = new Storage();
        this.tasks = storage.load();
    }

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

    public int size() { return tasks.size(); }

    public List<String> list() {
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add((i + 1) + ". " + tasks.get(i));
        }
        return lines;
    }

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

    public String tasksOnDateLabel(String dateStr) {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        return DateTimeParser.formatDate(targetDate.atStartOfDay());
    }

    public Task get(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        return tasks.get(taskIndex);
    }

    public void mark(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.get(taskIndex).mark();
        storage.save(tasks);
    }

    public void unmark(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.get(taskIndex).unmark();
        storage.save(tasks);
    }

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
