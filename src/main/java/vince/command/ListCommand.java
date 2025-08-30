package vince.command;

import java.util.List;
import vince.storage.TaskList;
import vince.ui.Ui;
import vince.exception.VinceException;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        List<String> lines = tasks.list();
        ui.showTaskList(lines);
    }
}
