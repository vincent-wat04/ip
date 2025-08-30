public class UnmarkCommand extends Command {
    private final String index;

    public UnmarkCommand(String index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        tasks.unmark(index);
        ui.showTaskUnmarked(tasks.get(index));
    }
}
