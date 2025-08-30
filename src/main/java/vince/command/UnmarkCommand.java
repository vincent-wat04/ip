package vince.command;

import vince.storage.TaskList;
import vince.ui.Ui;
import vince.task.Task;
import vince.exception.VinceException;

public class UnmarkCommand extends Command {
    private final String index;

    public UnmarkCommand(String index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        Task task = tasks.get(index);
        tasks.unmark(index);
        ui.showTaskUnmarked(task);
    }
}
