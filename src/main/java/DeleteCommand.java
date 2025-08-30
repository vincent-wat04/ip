public class DeleteCommand extends Command {
    private final String index;

    public DeleteCommand(String index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        Task deleted = tasks.delete(index);
        ui.showTaskDeleted(deleted, tasks.size());
    }
}
