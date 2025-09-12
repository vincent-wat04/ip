package vince;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import vince.storage.TaskList;
import vince.command.Command;
import vince.util.Parser;
import vince.exception.VinceException;
import vince.task.Task;
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
        // Initialize images with fallback
        try {
            user = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
        } catch (Exception e) {
            // Create a simple placeholder image if resource not found
            user = new Image(
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
        }

        try {
            vince = new Image(this.getClass().getResourceAsStream("/images/DaVince.png"));
        } catch (Exception e) {
            // Create a simple placeholder image if resource not found
            vince = new Image(
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
        }

        // Step 1. Setting up required components

        // The container for the content of the chat to scroll.
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        // Step 2. Formatting the window to look as expected
        stage.setTitle("Vince");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        // You will need to import `javafx.scene.layout.Region` for this.
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setBottomAnchor(userInput, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);

        // Step 3. Add functionality to handle user input.
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        userInput.setOnAction((event) -> {
            handleUserInput();
        });

        // Scroll down to the end every time dialogContainer's height changes.
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // Show welcome message
        showWelcomeMessage();
    }

    /**
     * Iteration 1: Creates a label with the specified text and adds it to the
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
     * Iteration 2: Creates two dialog boxes, one echoing user input and the other
     * containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        Label userText = new Label(userInput.getText());
        Label vinceText = new Label(getResponse(userInput.getText()));
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, new ImageView(user)),
                DialogBox.getVinceDialog(vinceText, new ImageView(vince)));
        userInput.clear();
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    private String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Oops! The command cannot be empty!";
        }

        try {
            Command command = Parser.parse(input);
            if (command == null) {
                return "Oops! It's an invalid command :-(";
            }

            // Create a simple string-based response system for GUI
            StringBuilder response = new StringBuilder();

            if (command instanceof vince.command.ExitCommand) {
                Platform.exit();
                return "Bye. Hope to see you again soon!";
            } else if (command instanceof vince.command.ListCommand) {
                var lines = tasks.list();
                if (lines.isEmpty()) {
                    response.append("Your task list is empty!");
                } else {
                    response.append("Here are the tasks in your list:\n");
                    response.append(lines.stream().collect(Collectors.joining("\n")));
                }
            } else if (command instanceof vince.command.AddCommand) {
                var addCommand = (vince.command.AddCommand) command;
                Task newTask = tasks.addTask(addCommand.getInput());
                response.append("Got it. I've added this task:\n");
                response.append(newTask).append("\n");
                response.append("Now you have ").append(tasks.size()).append(" tasks in the list.");
            } else if (command instanceof vince.command.MarkCommand) {
                var markCommand = (vince.command.MarkCommand) command;
                tasks.mark(markCommand.getIndex());
                Task task = tasks.get(markCommand.getIndex());
                response.append("Nice! I've marked this task as done:\n");
                response.append(task);
            } else if (command instanceof vince.command.UnmarkCommand) {
                var unmarkCommand = (vince.command.UnmarkCommand) command;
                tasks.unmark(unmarkCommand.getIndex());
                Task task = tasks.get(unmarkCommand.getIndex());
                response.append("OK, I've marked this task as not done yet:\n");
                response.append(task);
            } else if (command instanceof vince.command.DeleteCommand) {
                var deleteCommand = (vince.command.DeleteCommand) command;
                Task deletedTask = tasks.delete(deleteCommand.getIndex());
                response.append("Noted. I've removed this task:\n");
                response.append(deletedTask).append("\n");
                response.append("Now you have ").append(tasks.size()).append(" tasks in the list.");
            } else if (command instanceof vince.command.FindCommand) {
                var findCommand = (vince.command.FindCommand) command;
                var lines = tasks.findTasks(findCommand.getKeyword());
                if (lines.isEmpty()) {
                    response.append("No tasks found matching '").append(findCommand.getKeyword()).append("'.");
                } else {
                    response.append("Here are the matching tasks in your list:\n");
                    response.append(lines.stream().collect(Collectors.joining("\n")));
                }
            } else if (command instanceof vince.command.OnDateCommand) {
                var onDateCommand = (vince.command.OnDateCommand) command;
                var lines = tasks.tasksOnDateLines(onDateCommand.getDateStr());
                String dateLabel = tasks.tasksOnDateLabel(onDateCommand.getDateStr());
                if (lines.isEmpty()) {
                    response.append("No tasks found on ").append(dateLabel).append(".");
                } else {
                    response.append("Tasks on ").append(dateLabel).append(":\n");
                    response.append(lines.stream().collect(Collectors.joining("\n")));
                }
            }

            return response.toString().trim();

        } catch (VinceException e) {
            return "Oops! " + e.getMessage();
        }
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
