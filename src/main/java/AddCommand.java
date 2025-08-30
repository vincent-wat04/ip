public class AddCommand extends Command {
    private final String rawInput;

    public AddCommand(String rawInput) {
        this.rawInput = rawInput;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        Task added = tasks.addTask(rawInput);
        ui.showTaskAdded(added, tasks.size());
    }
}
