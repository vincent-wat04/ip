package vince;

import vince.ui.Ui;
import vince.storage.TaskList;
import vince.command.Command;
import vince.util.Parser;
import vince.exception.VinceException;

/**
 * Entry point of the Vince chatbot application.
 * Coordinates the main loop by reading input, parsing a Command,
 * executing it against the TaskList, and delegating output to the Ui.
 */
public class Vince {
    private Ui ui;
    private TaskList tasks;
    
    /**
     * Constructs a new Vince application with a fresh Ui and TaskList.
     */
    public Vince() {
        this.ui = new Ui();
        this.tasks = new TaskList();
    }
    
    /**
     * Runs the main interaction loop until an exit command is issued.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            if (input == null || input.trim().isEmpty()) {
                ui.showEmptyCommandError();
                continue;
            }
            try {
                Command command = Parser.parse(input);
                if (command == null) {
                    ui.showInvalidCommandError();
                    continue;
                }
                command.execute(tasks, ui);
                isExit = command.isExit();
            } catch (VinceException e) {
                ui.showError(e.getMessage());
            }
        }
    }
    
    /**
     * Launches the Vince application.
     * @param args CLI args (unused)
     */
    public static void main(String[] args) {
        new Vince().run();
    }
}
