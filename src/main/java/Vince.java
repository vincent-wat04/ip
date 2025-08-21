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
        while (true) {
            String input = scanner.nextLine();
            
            switch (input) {
                case "bye":
                    exit();
                    scanner.close();
                    return;
                default:
                    System.out.println("____________________________________________________________");
                    System.out.println(input);
                    System.out.println("____________________________________________________________\n");
                    break;
            }
        }
    }
}
