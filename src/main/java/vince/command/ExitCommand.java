package vince.command;

import vince.storage.TaskList;
import vince.ui.Ui;
import vince.exception.VinceException;

public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        ui.showGoodbye();
    }
    
    @Override
    public boolean isExit() {
        return true;
    }
}
