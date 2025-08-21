public class ToDoList {
    private int taskCount = 0;
    private String[] tasks = new String[100];
    
    public void addTask(String task) {
        tasks[taskCount] = task;
        taskCount++;
    }
    
    public void listTasks() {
        System.out.println("____________________________________________________________");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
        System.out.println("____________________________________________________________\n");
    }
}
