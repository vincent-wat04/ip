import java.util.List;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        List<String> lines = tasks.list();
        ui.showTaskList(lines);
    }
}
