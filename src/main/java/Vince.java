public class Vince {
    private Ui ui;
    private TaskList tasks;
    
    public Vince() {
        this.ui = new Ui();
        this.tasks = new TaskList();
    }
    
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
                ui.showLine();
                command.execute(tasks, ui);
                isExit = command.isExit();
            } catch (VinceException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }
    
    public static void main(String[] args) {
        new Vince().run();
    }
}
