public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui) throws VinceException;
    public boolean isExit() { return false; }
}
