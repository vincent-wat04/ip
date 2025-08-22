import java.util.Scanner;

public class Vince {
    public static void greet() {
        System.out.println("____________________________________________________________");
        System.out.println("Hello I'm Vince\n" +
                "What can I do for you?\n");
        System.out.println("____________________________________________________________\n");
    }
    
    public static void exit() {
        System.out.println("____________________________________________________________");
        System.out.println("Bye. Hope to see you again soon!\n");
        System.out.println("____________________________________________________________\n");
    }
    
    public static void main(String[] args) {
        greet();
        Scanner scanner = new Scanner(System.in);
        ToDoList toDoList = new ToDoList();
        while (true) {
            String input = scanner.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("____________________________________________________________");
                System.out.println("Oops! The command cannot be empty!");
                System.out.println("____________________________________________________________\n");
                continue;
            }

            String[] parts = input.split(" ");
            String command = parts[0];
            
            try {
                switch (command) {
                    case "bye":
                        exit();
                        scanner.close();
                        return;
                    case "list":
                        toDoList.listTasks();
                        break;
                    case "mark":
                        toDoList.markTask(parts[1]);
                        System.out.println("____________________________________________________________");
                        System.out.println("Nice! I've marked this task as done: " );
                        System.out.println(toDoList.getTask(parts[1]));
                        System.out.println("____________________________________________________________\n");
                        break;
                    case "unmark":
                        toDoList.unmarkTask(parts[1]);
                        System.out.println("____________________________________________________________");
                        System.out.println("OK, I've marked this task as not done yet: ");
                        System.out.println(toDoList.getTask(parts[1]));
                        System.out.println("____________________________________________________________\n");
                        break;
                    case "delete":
                        Task deletedTask = toDoList.deleteTask(parts[1]);
                        System.out.println("____________________________________________________________");
                        System.out.println("Noted. I've removed this task:\n" + deletedTask);
                        System.out.println("Now you have " + toDoList.getTaskCount() + " tasks in the list.");
                        System.out.println("____________________________________________________________\n");
                        break;
                    case "todo":
                    case "deadline":
                    case "event":   
                        Task addedtask = toDoList.addTask(input);
                        System.out.println("____________________________________________________________");
                        System.out.println("Got it. I've added this task:\n" + addedtask);
                        System.out.println("Now you have " + toDoList.getTaskCount() + " tasks in the list.");
                        System.out.println("____________________________________________________________\n");
                        break;
    
                    default:
                        System.out.println("____________________________________________________________");
                        System.out.println("Oops! It's an invalid command :-(");
                        System.out.println("____________________________________________________________\n");
                        break;
                }
            } catch (VinceException e) {
                System.out.println("____________________________________________________________");
                System.out.println("Oops! " + e.getMessage());
                System.out.println("____________________________________________________________\n");
            } 
        }
    }
}
