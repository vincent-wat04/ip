package vince.command;

import java.util.List;
import vince.storage.TaskList;
import vince.ui.Ui;
import vince.exception.VinceException;

public class OnDateCommand extends Command {
    private final String dateStr;

    public OnDateCommand(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        String label = tasks.tasksOnDateLabel(dateStr);
        List<String> lines = tasks.tasksOnDateLines(dateStr);
        ui.showTasksOnDate(label, lines);
    }
}
