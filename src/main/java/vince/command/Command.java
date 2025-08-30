package vince.command;

import vince.storage.TaskList;
import vince.ui.Ui;
import vince.exception.VinceException;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui) throws VinceException;
    public boolean isExit() { return false; }
}
