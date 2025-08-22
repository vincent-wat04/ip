public class ToDoList {
    private int taskCount = 0;
    private Task[] tasks = new Task[100];
    
    public Task addTask(String task) throws VinceException {
        String type = task.split(" ")[0];
        switch (type) {
            case "deadline":
                if (!task.contains(" /by ")) {
                    throw new VinceException("Deadlisne task must contain ' /by ' to specify the deadline!");
                }
                String[] deadlineParts = task.split(" /by ");
                tasks[taskCount] = new Deadline(deadlineParts[0].substring(9), deadlineParts[1]);
                break;
            case "event":
                if (!task.contains(" /from ") || !task.contains(" /to ")) {
                    throw new VinceException("Event task must contain ' /from ' and ' /to ' to specify the event time!");
                }
                String[] eventParts = task.split(" /from | /to ");
                tasks[taskCount] = new Event(eventParts[0].substring(6), eventParts[1], eventParts[2]);
                break;
            case "todo":    
                if (task.length() < 5) {
                    throw new VinceException("ToDo task must start with 'todo ' and have a description!");
                }
                tasks[taskCount] = new Todo(task.substring(5));
                break;
            default:
                tasks[taskCount] = new Task(task);
        }
        taskCount++;
        return tasks[taskCount - 1];
    }

    public int getTaskCount() {
        return taskCount;
    }
    
    public void listTasks() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
        System.out.println("____________________________________________________________\n");
    }

    public Task getTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + taskCount + " tasks.");
        }
        return tasks[taskIndex];
    }

    public void markTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + taskCount + " tasks.");
        }
        tasks[taskIndex].mark();
    }

    public void unmarkTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new VinceException("Task index " + (taskIndex + 1) + " is out of range! You have " + taskCount + " tasks.");
        }
        tasks[taskIndex].unmark();
    }
}

