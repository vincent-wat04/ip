package vince.ui;

import java.util.Scanner;
import java.util.List;
import vince.task.Task;

public class Ui {
    private Scanner scanner;
    
    public Ui() {
        this.scanner = new Scanner(System.in);
    }
    
    public void showWelcome() {
        showLine();
        System.out.println("Hello I'm Vince");
        System.out.println("What can I do for you?");
        System.out.println();
        showLine();
        System.out.println();
    }
    
    public void showGoodbye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println();
        showLine();
        System.out.println();
    }
    
    public String readCommand() {
        return scanner.nextLine();
    }
    
    public void showLine() {
        System.out.println("____________________________________________________________");
    }
    
    public void showError(String message) {
        showLine();
        System.out.println("Oops! " + message);
        showLine();
        System.out.println();
    }
    
    public void showEmptyCommandError() {
        showLine();
        System.out.println("Oops! The command cannot be empty!");
        showLine();
        System.out.println();
    }
    
    public void showInvalidCommandError() {
        showLine();
        System.out.println("Oops! It's an invalid command :-(");
        showLine();
        System.out.println();
    }
    
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
        System.out.println();
    }
    
    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
        showLine();
        System.out.println();
    }
    
    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
        showLine();
        System.out.println();
    }
    
    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
        System.out.println();
    }
    
    public void showDateRequiredError() {
        showLine();
        System.out.println("Please specify a date! For instance, 'on <date>'");
        showLine();
        System.out.println();
    }

    public void showTaskList(List<String> lines) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        for (String line : lines) {
            System.out.println(line);
        }
        showLine();
        System.out.println();
    }

    public void showTasksOnDate(String dateLabel, List<String> lines) {
        showLine();
        System.out.println("Tasks on " + dateLabel + ":");
        if (lines.isEmpty()) {
            System.out.println("No tasks found on this date.");
        } else {
            for (String line : lines) {
                System.out.println(line);
            }
        }
        showLine();
        System.out.println();
    }
    
    public void close() {
        scanner.close();
    }
}
