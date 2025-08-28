import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Task> tasks = new ArrayList<Task>();
    
    public ToDoList() {
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
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("____________________________________________________________\n");
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

