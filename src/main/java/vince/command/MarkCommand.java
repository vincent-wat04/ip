package vince.command;

import vince.storage.TaskList;
import vince.ui.Ui;
import vince.task.Task;
import vince.exception.VinceException;

public class MarkCommand extends Command {
    private final String index;

    public MarkCommand(String index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        Task task = tasks.get(index);
        tasks.mark(index);
        ui.showTaskMarked(task);
    }
}
