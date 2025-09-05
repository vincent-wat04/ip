package vince.command;

import vince.storage.TaskList;
import vince.ui.Ui;
import vince.task.Task;
import vince.exception.VinceException;

public class AddCommand extends Command {
    private final String rawInput;

    public AddCommand(String rawInput) {
        this.rawInput = rawInput;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        Task added = tasks.addTask(rawInput);
        ui.showTaskAdded(added, tasks.size());
    }

    public String getInput() {
        return rawInput;
    }
}
