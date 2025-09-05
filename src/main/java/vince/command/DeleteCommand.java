package vince.command;

import vince.storage.TaskList;
import vince.ui.Ui;
import vince.task.Task;
import vince.exception.VinceException;

public class DeleteCommand extends Command {
    private final String index;

    public DeleteCommand(String index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        Task deleted = tasks.delete(index);
        ui.showTaskDeleted(deleted, tasks.size());
    }

    public String getIndex() {
        return index;
    }
}
