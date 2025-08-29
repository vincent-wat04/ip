import java.util.Scanner;

public class Vince {
    private Ui ui;
    private ToDoList toDoList;
    
    public Vince() {
        this.ui = new Ui();
        this.toDoList = new ToDoList(ui);
    }
    
    public void run() {
        ui.showWelcome();
        
        while (true) {
            String input = ui.readCommand();

            if (input == null || input.trim().isEmpty()) {
                ui.showEmptyCommandError();
                continue;
            }

            String[] parts = input.split(" ");
            String commandString = parts[0];
            
            try {
                if (Command.fromString(commandString) == null) {
                    ui.showInvalidCommandError();
                    continue;
                }
                
                Command command = Command.fromString(commandString);
                switch (command) {
                    case BYE:
                        ui.showGoodbye();
                        ui.close();
                        return;
                    case LIST:
                        toDoList.listTasks();
                        break;
                    case MARK:
                        toDoList.markTask(parts[1]);
                        ui.showTaskMarked(toDoList.getTask(parts[1]));
                        break;
                    case UNMARK:
                        toDoList.unmarkTask(parts[1]);
                        ui.showTaskUnmarked(toDoList.getTask(parts[1]));
                        break;
                    case DELETE:
                        Task deletedTask = toDoList.deleteTask(parts[1]);
                        ui.showTaskDeleted(deletedTask, toDoList.getTaskCount());
                        break;
                    case ON:
                        if (parts.length < 2) {
                            ui.showDateRequiredError();
                            break;
                        }
                        String dateStr = input.substring(3).trim(); // Remove "on "
                        toDoList.showTasksOnDate(dateStr);
                        break;
                    case TODO:
                    case DEADLINE:
                    case EVENT:   
                        Task addedtask = toDoList.addTask(input);
                        ui.showTaskAdded(addedtask, toDoList.getTaskCount());
                        break;
                }
            } catch (VinceException e) {
                ui.showError(e.getMessage());
            } 
        }
    }
    
    public static void main(String[] args) {
        new Vince().run();
    }
}
