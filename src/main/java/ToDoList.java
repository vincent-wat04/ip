import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private Ui ui;
    
    public ToDoList(Ui ui) {
        this.ui = ui;
        this.tasks = Storage.load();
    }
    
    public Task addTask(String task) throws VinceException {
        String type = task.split(" ")[0];
        
        TaskType taskType = TaskType.fromCommand(type);
        if (taskType == null) {
            tasks.add(new Task(task));
            Storage.save(tasks);
            return tasks.get(tasks.size() - 1);
        }
        
        switch (taskType) {
            case DEADLINE:
                if (!task.contains(" /by ")) {
                    throw new VinceException("Deadline task must contain ' /by ' to specify the deadline!");
                }
                String[] deadlineParts = task.split(" /by ");
                tasks.add(new Deadline(deadlineParts[0].substring(taskType.getPrefixLength()), deadlineParts[1]));
                break;
            case EVENT:
                if (!task.contains(" /from ") || !task.contains(" /to ")) {
                    throw new VinceException("Event task must contain ' /from ' and ' /to ' to specify the event time!");
                }
                String[] eventParts = task.split(" /from | /to ");
                tasks.add(new Event(eventParts[0].substring(taskType.getPrefixLength()), eventParts[1], eventParts[2]));
                break;
            case TODO:    
                if (task.length() < taskType.getPrefixLength()) {
                    throw new VinceException("Todo task must start with 'todo ' and have a description!");
                }
                tasks.add(new Todo(task.substring(taskType.getPrefixLength())));
                break;
        }
        Storage.save(tasks);
        return tasks.get(tasks.size() - 1);
    }

    public int getTaskCount() {
        return tasks.size();
    }
    
    public void listTasks() {
        ui.showLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
        System.out.println();
    }

    public void showTasksOnDate(String dateStr) throws VinceException {
        LocalDate targetDate = DateTimeParser.parseDateTime(dateStr).toLocalDate();
        
        ui.showLine();
        System.out.println("Tasks on " + DateTimeParser.formatDate(targetDate.atStartOfDay()) + ":");
        
        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            LocalDateTime taskDate = null;
            
            if (task instanceof Deadline) {
                taskDate = ((Deadline) task).getBy();
            } else if (task instanceof Event) {
                Event event = (Event) task;
                // Check if the event spans the target date
                LocalDate eventStartDate = event.getFrom().toLocalDate();
                LocalDate eventEndDate = event.getTo().toLocalDate();
                if (!targetDate.isBefore(eventStartDate) && !targetDate.isAfter(eventEndDate)) {
                    System.out.println((i + 1) + ". " + task);
                    found = true;
                }
                continue;
            }
            
            if (taskDate != null && taskDate.toLocalDate().equals(targetDate)) {
                System.out.println((i + 1) + ". " + task);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No tasks found on this date.");
        }
        ui.showLine();
        System.out.println();
    }

    public Task getTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        return tasks.get(taskIndex);
    }

    public void markTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.get(taskIndex).mark();
        Storage.save(tasks);
    }

    public void unmarkTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.get(taskIndex).unmark();
        Storage.save(tasks);
    }

    public Task deleteTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + tasks.size() + " tasks.");
        }
        Task removed = tasks.remove(taskIndex);
        Storage.save(tasks);
        return removed;
    }
}

