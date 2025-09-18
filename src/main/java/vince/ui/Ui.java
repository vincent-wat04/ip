package vince.ui;

import java.util.Scanner;
import java.util.List;
import vince.task.Task;

/**
 * Handles all user interaction concerns: printing messages, reading commands,
 * and formatting lists for display. Keeps I/O concerns isolated from logic.
 */
public class Ui {
    // Constants for UI messages
    private static final String WELCOME_MESSAGE = "Hello I'm Vince";
    private static final String HELP_MESSAGE = "What can I do for you?";
    private static final String GOODBYE_MESSAGE = "Bye. Hope to see you again soon!";
    private static final String EMPTY_COMMAND_ERROR = "Oops! The command cannot be empty!";
    private static final String INVALID_COMMAND_ERROR = "Oops! It's an invalid command :-(";
    private static final String DIVIDER_LINE = "____________________________________________________________";
    
    private Scanner scanner;
    
    /**
     * Creates a UI bound to standard input and output streams.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }
    
    /** Prints the welcome banner. */
    public void showWelcome() {
        showLine();
        System.out.println(WELCOME_MESSAGE);
        System.out.println(HELP_MESSAGE);
        System.out.println();
        showLine();
        System.out.println();
    }
    
    /** Prints the goodbye banner and trailing divider. */
    public void showGoodbye() {
        showLine();
        System.out.println(GOODBYE_MESSAGE);
        System.out.println();
        showLine();
        System.out.println();
    }
    
    /**
     * Reads a single line command from the user.
     * @return the raw command string
     */
    public String readCommand() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return null;
        }
    }
    
    /** Prints a horizontal divider line. */
    public void showLine() {
        System.out.println(DIVIDER_LINE);
    }
    
    /**
     * Prints an error message framed by divider lines.
     * @param message explanation of the error
     */
    public void showError(String message) {
        showLine();
        System.out.println("Oops! " + message);
        showLine();
        System.out.println();
    }
    
    /** Prints an error for empty commands. */
    public void showEmptyCommandError() {
        showLine();
        System.out.println(EMPTY_COMMAND_ERROR);
        showLine();
        System.out.println();
    }
    
    /** Prints an error for unknown commands. */
    public void showInvalidCommandError() {
        showLine();
        System.out.println(INVALID_COMMAND_ERROR);
        showLine();
        System.out.println();
    }
    
    /**
     * Shows a confirmation that a task was added.
     * @param task the task added
     * @param taskCount new size of the task list
     */
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
        System.out.println();
    }
    
    /**
     * Shows that a task was marked done.
     * @param task the task that has been marked
     */
    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
        showLine();
        System.out.println();
    }
    
    /**
     * Shows that a task was unmarked.
     * @param task the task that has been unmarked
     */
    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
        showLine();
        System.out.println();
    }
    
    /**
     * Shows a confirmation that a task was deleted.
     * @param task the task removed
     * @param taskCount resulting size of the task list
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
        System.out.println();
    }
    
    /** Prints an error that a date is required for this operation. */
    public void showDateRequiredError() {
        showLine();
        System.out.println("Please specify a date! For instance, 'on <date>'");
        showLine();
        System.out.println();
    }

    /**
     * Displays a numbered list of tasks.
     * @param lines preformatted task lines (e.g. "1. [T] read book")
     */
    public void showTaskList(List<String> lines) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        for (String line : lines) {
            System.out.println(line);
        }
        showLine();
        System.out.println();
    }

    /**
     * Displays the set of tasks that occur on a specific date.
     * @param dateLabel formatted date label (e.g., "Dec 15 2024")
     * @param lines preformatted task lines for that date
     */
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

    /**
     * Displays a schedule view for tasks on a specific date.
     * @param dateLabel formatted date label (e.g., "Dec 15 2024")
     * @param scheduleLines list of schedule lines with timeline
     */
    public void showSchedule(String dateLabel, List<String> scheduleLines) {
        showLine();
        System.out.println("📅 Schedule for " + dateLabel + ":");
        System.out.println();
        for (String line : scheduleLines) {
            System.out.println(line);
        }
        showLine();
        System.out.println();
    }
    
    /**
     * Shows AI-enhanced help with command examples and tips.
     */
    public void showHelp() {
        showLine();
        System.out.println("🤖 Vince AI Assistant - Available Commands:");
        System.out.println();
        System.out.println("📝 Task Management:");
        System.out.println("  • todo <description> - Add a simple task");
        System.out.println("  • deadline <description> /by <date> - Add a task with deadline");
        System.out.println("  • event <description> /from <start> /to <end> - Add a scheduled event");
        System.out.println();
        System.out.println("📋 Task Operations:");
        System.out.println("  • list - Show all tasks");
        System.out.println("  • mark <number> - Mark task as completed");
        System.out.println("  • unmark <number> - Mark task as incomplete");
        System.out.println("  • delete <number> - Remove a task");
        System.out.println();
        System.out.println("🔍 Smart Features:");
        System.out.println("  • find <keyword> - Search tasks by keyword");
        System.out.println("  • schedule <date> - View timeline for a specific date");
        System.out.println("  • on <date> - List tasks on a specific date");
        System.out.println();
        System.out.println("🧠 Natural Language Support:");
        System.out.println("  • Use 'today', 'tomorrow', 'next friday'");
        System.out.println("  • Use '3pm', '1400', '2:30pm' for times");
        System.out.println("  • Priority auto-detection from task content");
        System.out.println();
        System.out.println("💡 Examples:");
        System.out.println("  • deadline Submit report /by tomorrow 5pm");
        System.out.println("  • event Team meeting /from today 2pm /to today 3pm");
        System.out.println("  • schedule next monday");
        showLine();
        System.out.println();
    }
    
    /**
     * Shows AI-powered suggestions to improve productivity.
     * @param suggestions list of personalized suggestions
     */
    public void showSuggestions(List<String> suggestions) {
        showLine();
        System.out.println("🧠 AI Suggestions for You:");
        System.out.println();
        for (String suggestion : suggestions) {
            System.out.println("  " + suggestion);
        }
        showLine();
        System.out.println();
    }
    
    /** Closes the scanner backing this UI. */
    public void close() {
        scanner.close();
    }
}
