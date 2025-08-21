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
            String[] parts = input.split(" ");
            String command = parts[0];
            
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
                default:
                    toDoList.addTask(input);
                    System.out.println("____________________________________________________________");
                    System.out.println("added: " + input);
                    System.out.println("____________________________________________________________\n");
                    break;
            }
        }
    }
}
