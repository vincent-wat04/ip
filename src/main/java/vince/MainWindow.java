package vince;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import vince.storage.TaskList;
import vince.command.Command;
import vince.util.Parser;
import vince.exception.VinceException;
import vince.task.Task;
import vince.ai.TaskSuggestionEngine;
import java.util.stream.Collectors;

/**
 * Controller for the main GUI window.
 * Handles user interactions and coordinates between UI and business logic.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private TaskList tasks;
    private Image userImage;
    private Image vinceImage;

    /**
     * Initializes the controller after FXML loading.
     * Sets up scroll behavior and other UI properties.
     */
    @FXML
    public void initialize() {
        // Auto-scroll to bottom when new content is added
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the TaskList instance for task management.
     * 
     * @param tasks the task list to use
     */
    public void setTaskList(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Sets the user and bot avatar images.
     * 
     * @param userImage image for user messages
     * @param vinceImage image for Vince responses
     */
    public void setImages(Image userImage, Image vinceImage) {
        this.userImage = userImage;
        this.vinceImage = vinceImage;
    }

    /**
     * Shows the welcome message when the application starts.
     */
    public void showWelcomeMessage() {
        String welcomeText = "Hello I'm Vince\nWhat can I do for you?";
        addVinceDialog(welcomeText);
    }

    /**
     * Creates two dialog boxes for user input and Vince's response,
     * then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = getResponse(input);
        
        addUserDialog(input);
        addVinceDialog(response);
        
        userInput.clear();
    }

    /**
     * Adds a user dialog box to the conversation.
     * 
     * @param text the user's input text
     */
    private void addUserDialog(String text) {
        Label userText = new Label(text);
        VBox userContainer = new VBox(DialogBox.getUserDialog(userText, new ImageView(userImage)));
        userContainer.setAlignment(Pos.CENTER_RIGHT);
        dialogContainer.getChildren().add(userContainer);
    }

    /**
     * Adds a Vince dialog box to the conversation.
     * 
     * @param text Vince's response text
     */
    private void addVinceDialog(String text) {
        Label vinceText = new Label(text);
        VBox vinceContainer = new VBox(DialogBox.getVinceDialog(vinceText, new ImageView(vinceImage)));
        vinceContainer.setAlignment(Pos.CENTER_LEFT);
        dialogContainer.getChildren().add(vinceContainer);
    }

    /**
     * Generates a response to user input by parsing and executing the command.
     * This method handles the GUI-specific response generation for all command types.
     * 
     * @param input the raw user input string
     * @return formatted response string for display in GUI
     */
    private String getResponse(String input) {
        if (isInputEmpty(input)) {
            return "Oops! The command cannot be empty!";
        }

        try {
            Command command = Parser.parse(input);
            if (command == null) {
                return "Oops! It's an invalid command :-(";
            }

            return executeCommandAndGetResponse(command);

        } catch (VinceException e) {
            return "Oops! " + e.getMessage();
        }
    }

    /**
     * Checks if the input string is null or empty after trimming.
     * 
     * @param input the input string to check
     * @return true if input is null or empty, false otherwise
     */
    private boolean isInputEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Executes the given command and returns the appropriate response string.
     * 
     * @param command the parsed command to execute
     * @return response string for the executed command
     */
    private String executeCommandAndGetResponse(Command command) {
        if (command instanceof vince.command.ExitCommand) {
            return handleExitCommand();
        } else if (command instanceof vince.command.ListCommand) {
            return handleListCommand();
        } else if (command instanceof vince.command.AddCommand) {
            return handleAddCommand((vince.command.AddCommand) command);
        } else if (command instanceof vince.command.MarkCommand) {
            return handleMarkCommand((vince.command.MarkCommand) command);
        } else if (command instanceof vince.command.UnmarkCommand) {
            return handleUnmarkCommand((vince.command.UnmarkCommand) command);
        } else if (command instanceof vince.command.DeleteCommand) {
            return handleDeleteCommand((vince.command.DeleteCommand) command);
        } else if (command instanceof vince.command.FindCommand) {
            return handleFindCommand((vince.command.FindCommand) command);
        } else if (command instanceof vince.command.OnDateCommand) {
            return handleOnDateCommand((vince.command.OnDateCommand) command);
        } else if (command instanceof vince.command.ScheduleCommand) {
            return handleScheduleCommand((vince.command.ScheduleCommand) command);
        } else if (command instanceof vince.command.HelpCommand) {
            return handleHelpCommand();
        }

        return "Unknown command type";
    }

    /**
     * Handles the exit command by terminating the application.
     * 
     * @return goodbye message
     */
    private String handleExitCommand() {
        javafx.application.Platform.exit();
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Handles the list command by displaying all tasks.
     * 
     * @return formatted list of tasks or empty message
     */
    private String handleListCommand() {
        var taskLines = tasks.list();
        if (taskLines.isEmpty()) {
            return "Your task list is empty!";
        }

        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (String line : taskLines) {
            response.append(line).append("\n");
        }
        return response.toString().trim();
    }

    /**
     * Handles the add command by creating a new task.
     * 
     * @param addCommand the add command containing task details
     * @return confirmation message with task details
     */
    private String handleAddCommand(vince.command.AddCommand addCommand) {
        Task newTask = tasks.addTask(addCommand.getInput());
        return String.format("Got it. I've added this task:\n%s\nNow you have %d tasks in the list.",
                newTask, tasks.size());
    }

    /**
     * Handles the mark command by marking a task as done.
     * 
     * @param markCommand the mark command with task index
     * @return confirmation message with marked task
     */
    private String handleMarkCommand(vince.command.MarkCommand markCommand) {
        tasks.mark(markCommand.getIndex());
        Task markedTask = tasks.get(markCommand.getIndex());
        return String.format("Nice! I've marked this task as done:\n%s", markedTask);
    }

    /**
     * Handles the unmark command by marking a task as not done.
     * 
     * @param unmarkCommand the unmark command with task index
     * @return confirmation message with unmarked task
     */
    private String handleUnmarkCommand(vince.command.UnmarkCommand unmarkCommand) {
        tasks.unmark(unmarkCommand.getIndex());
        Task unmarkedTask = tasks.get(unmarkCommand.getIndex());
        return String.format("OK, I've marked this task as not done yet:\n%s", unmarkedTask);
    }

    /**
     * Handles the delete command by removing a task.
     * 
     * @param deleteCommand the delete command with task index
     * @return confirmation message with deleted task
     */
    private String handleDeleteCommand(vince.command.DeleteCommand deleteCommand) {
        Task deletedTask = tasks.delete(deleteCommand.getIndex());
        return String.format("Noted. I've removed this task:\n%s\nNow you have %d tasks in the list.",
                deletedTask, tasks.size());
    }

    /**
     * Handles the find command by searching for tasks with matching keywords.
     * 
     * @param findCommand the find command with search keyword
     * @return formatted list of matching tasks or no results message
     */
    private String handleFindCommand(vince.command.FindCommand findCommand) {
        var matchingLines = tasks.findTasks(findCommand.getKeyword());
        if (matchingLines.isEmpty()) {
            return String.format("No tasks found matching '%s'.", findCommand.getKeyword());
        }

        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (String line : matchingLines) {
            response.append(line).append("\n");
        }
        return response.toString().trim();
    }

    /**
     * Handles the on date command by showing tasks on a specific date.
     * 
     * @param onDateCommand the on date command with date string
     * @return formatted list of tasks on the date or no results message
     */
    private String handleOnDateCommand(vince.command.OnDateCommand onDateCommand) {
        var taskLines = tasks.tasksOnDateLines(onDateCommand.getDateStr());
        String dateLabel = tasks.tasksOnDateLabel(onDateCommand.getDateStr());

        if (taskLines.isEmpty()) {
            return String.format("No tasks found on %s.", dateLabel);
        }

        StringBuilder response = new StringBuilder(String.format("Tasks on %s:\n", dateLabel));
        for (String line : taskLines) {
            response.append(line).append("\n");
        }
        return response.toString().trim();
    }

    /**
     * Handles the schedule command by showing a timeline view of tasks on a specific date.
     * 
     * @param scheduleCommand the schedule command with date string
     * @return formatted schedule with timeline or no results message
     */
    private String handleScheduleCommand(vince.command.ScheduleCommand scheduleCommand) {
        var scheduleLines = tasks.getScheduleForDate(scheduleCommand.getDateStr());
        String dateLabel = tasks.tasksOnDateLabel(scheduleCommand.getDateStr());

        StringBuilder response = new StringBuilder(String.format("📅 Schedule for %s:\n\n", dateLabel));
        for (String line : scheduleLines) {
            response.append(line).append("\n");
        }
        return response.toString().trim();
    }

    /**
     * Handles the help command by showing AI-enhanced help and suggestions.
     * 
     * @return formatted help text with personalized suggestions
     */
    private String handleHelpCommand() {
        StringBuilder response = new StringBuilder();

        // Add help content
        response.append("🤖 Vince AI Assistant - Available Commands:\n\n");
        response.append("📝 Task Management:\n");
        response.append("  • todo <description> - Add a simple task\n");
        response.append("  • deadline <description> /by <date> - Add a task with deadline\n");
        response.append("  • event <description> /from <start> /to <end> - Add a scheduled event\n\n");
        response.append("📋 Task Operations:\n");
        response.append("  • list - Show all tasks\n");
        response.append("  • mark <number> - Mark task as completed\n");
        response.append("  • unmark <number> - Mark task as incomplete\n");
        response.append("  • delete <number> - Remove a task\n\n");
        response.append("🔍 Smart Features:\n");
        response.append("  • find <keyword> - Search tasks by keyword\n");
        response.append("  • schedule <date> - View timeline for a specific date\n");
        response.append("  • on <date> - List tasks on a specific date\n\n");
        response.append("🧠 Natural Language Support:\n");
        response.append("  • Use 'today', 'tomorrow', 'next friday'\n");
        response.append("  • Use '3pm', '1400', '2:30pm' for times\n");
        response.append("  • Priority auto-detection from task content\n\n");
        response.append("💡 Examples:\n");
        response.append("  • deadline Submit report /by tomorrow 5pm\n");
        response.append("  • event Team meeting /from today 2pm /to today 3pm\n");
        response.append("  • schedule next monday\n\n");

        // Add AI suggestions
        var suggestions = TaskSuggestionEngine.generateSuggestions(tasks.getAllTasks());
        if (!suggestions.isEmpty()) {
            response.append("🧠 AI Suggestions for You:\n");
            for (String suggestion : suggestions) {
                response.append("  ").append(suggestion).append("\n");
            }
        }

        return response.toString().trim();
    }
}
