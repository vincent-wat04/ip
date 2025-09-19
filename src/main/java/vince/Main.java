package vince;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import vince.storage.TaskList;
import vince.command.Command;
import vince.util.Parser;
import vince.exception.VinceException;
import vince.task.Task;
import vince.ai.TaskSuggestionEngine;
import java.util.stream.Collectors;

/**
 * Main class for the JavaFX GUI application.
 */
public class Main extends Application {
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private TaskList tasks = new TaskList();

    private Image user;
    private Image vince;

    @Override
    public void start(Stage stage) {
        initializeImages();
        setupUIComponents();
        configureStage(stage);
        setupEventHandlers();
        showWelcomeMessage();
    }

    /**
     * Initializes user and bot avatar images with fallback placeholders.
     */
    private void initializeImages() {
        // Initialize user avatar image with fallback
        try {
            user = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
        } catch (Exception e) {
            // Create a simple placeholder image if resource not found
            user = new Image(
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
        }

        // Initialize Vince avatar image with fallback
        try {
            vince = new Image(this.getClass().getResourceAsStream("/images/DaVince.png"));
        } catch (Exception e) {
            // Create a simple placeholder image if resource not found
            vince = new Image(
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
        }
    }

    /**
     * Sets up and configures all UI components including layout and styling.
     */
    private void setupUIComponents() {
        // Create and configure the chat scroll area
        setupScrollPane();
        
        // Create input controls
        userInput = new TextField();
        sendButton = new Button("Send");
        
        // Create main layout and add all components
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);
        
        // Configure responsive sizing
        configureResponsiveLayout(mainLayout);
        
        // Create scene
        scene = new Scene(mainLayout);
    }

    /**
     * Sets up the scrollable chat area with dialog container.
     */
    private void setupScrollPane() {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        dialogContainer.setSpacing(5.0); // Add spacing between dialog boxes
        dialogContainer.setPadding(new Insets(10.0)); // Add padding around content
        scrollPane.setContent(dialogContainer);
    }

    /**
     * Configures responsive layout properties for all UI components.
     * 
     * @param mainLayout the main AnchorPane layout
     */
    private void configureResponsiveLayout(AnchorPane mainLayout) {
        // Set preferred size for main layout
        mainLayout.setPrefSize(450.0, 650.0);

        // Configure scroll pane properties
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        // Configure dialog container
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setMaxHeight(Region.USE_PREF_SIZE);

        // Configure input controls sizing
        userInput.setPrefWidth(Region.USE_COMPUTED_SIZE);
        sendButton.setPrefWidth(80.0);

        // Set up responsive anchoring
        setupAnchoring();
    }

    /**
     * Sets up anchor constraints for responsive positioning of UI components.
     */
    private void setupAnchoring() {
        // Anchor scroll pane to fill most of the window
        AnchorPane.setTopAnchor(scrollPane, 10.0);
        AnchorPane.setLeftAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);
        AnchorPane.setBottomAnchor(scrollPane, 60.0); // Leave space for input area

        // Position send button at bottom-right
        AnchorPane.setBottomAnchor(sendButton, 10.0);
        AnchorPane.setRightAnchor(sendButton, 10.0);

        // Position input field to fill available width
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setLeftAnchor(userInput, 10.0);
        AnchorPane.setRightAnchor(userInput, 100.0); // Leave space for send button
    }

    /**
     * Configures the stage (window) properties and displays it.
     * 
     * @param stage the primary stage to configure
     */
    private void configureStage(Stage stage) {
        // Set scene and show window
        stage.setScene(scene);
        stage.show();

        // Configure window properties
        stage.setTitle("Vince AI Assistant");
        stage.setResizable(true); // Enable window resizing
        
        // Set size constraints
        stage.setMinHeight(400.0); // Set reasonable minimum size
        stage.setMinWidth(350.0);
        stage.setMaxHeight(1000.0); // Set maximum size to prevent excessive stretching
        stage.setMaxWidth(800.0);
    }

    /**
     * Sets up event handlers for user interactions.
     */
    private void setupEventHandlers() {
        // Handle send button clicks
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        // Handle Enter key in text field
        userInput.setOnAction((event) -> {
            handleUserInput();
        });

        // Auto-scroll to bottom when new messages are added
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));
    }

    /**
     * Creates a label with the specified text and adds it to the
     * dialog container.
     * 
     * @param text String containing text to add
     * @return a label with the specified text that has word wrap enabled.
     */
    private Label getDialogLabel(String text) {
        Label textToAdd = new Label(text);
        textToAdd.setWrapText(true);

        return textToAdd;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other
     * containing Vince's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        Label userText = new Label(userInput.getText());
        Label vinceText = new Label(getResponse(userInput.getText()));

        // Create dialog boxes
        DialogBox userDialog = DialogBox.getUserDialog(userText, new ImageView(user));
        DialogBox vinceDialog = DialogBox.getVinceDialog(vinceText, new ImageView(vince));

        // Create containers for proper alignment
        VBox userContainer = new VBox(userDialog);
        userContainer.setAlignment(Pos.CENTER_RIGHT);

        VBox vinceContainer = new VBox(vinceDialog);
        vinceContainer.setAlignment(Pos.CENTER_LEFT);

        dialogContainer.getChildren().addAll(userContainer, vinceContainer);
        userInput.clear();
    }

    /**
     * Generates a response to user input by parsing and executing the command.
     * This method handles the GUI-specific response generation for all command
     * types.
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
        Platform.exit();
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
     * Handles the schedule command by showing a timeline view of tasks on a
     * specific date.
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

    /**
     * Shows the welcome message when the application starts.
     */
    private void showWelcomeMessage() {
        Label welcomeText = new Label("Hello I'm Vince\nWhat can I do for you?");
        dialogContainer.getChildren().addAll(
                DialogBox.getVinceDialog(welcomeText, new ImageView(vince)));
    }
}
