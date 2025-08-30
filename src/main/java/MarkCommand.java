public class MarkCommand extends Command {
    private final String index;

    public MarkCommand(String index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        tasks.mark(index);
        ui.showTaskMarked(tasks.get(index));
    }
}
