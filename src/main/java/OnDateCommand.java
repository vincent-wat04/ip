import java.util.List;

public class OnDateCommand extends Command {
    private final String dateStr;

    public OnDateCommand(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        String label = tasks.tasksOnDateLabel(dateStr);
        List<String> lines = tasks.tasksOnDateLines(dateStr);
        ui.showTasksOnDate(label, lines);
    }
}
