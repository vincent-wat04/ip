public class ToDoList {
    private int taskCount = 0;
    private Task[] tasks = new Task[100];
    
    public void addTask(String task) {
        tasks[taskCount] = new Task(task);
        taskCount++;
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

