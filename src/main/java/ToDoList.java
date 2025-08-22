public class ToDoList {
    private int taskCount = 0;
    private Task[] tasks = new Task[100];
    
    public Task addTask(String task) {
        String type = task.split(" ")[0];
        switch (type) {
            case "deadline":
                String[] deadlineParts = task.split(" /by ");
                tasks[taskCount] = new Deadline(deadlineParts[0].substring(9), deadlineParts[1]);
                break;
            case "event":
                String[] eventParts = task.split(" /from | /to ");
                tasks[taskCount] = new Event(eventParts[0].substring(6), eventParts[1], eventParts[2]);
                break;
            case "todo":
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
        return tasks[taskIndex];
    }

    public void markTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        tasks[taskIndex].mark();
    }

    public void unmarkTask(String index) {
        int taskIndex = Integer.parseInt(index) - 1;
        tasks[taskIndex].unmark();
    }
}

