package vince.command;

import java.util.List;
import vince.storage.TaskList;
import vince.ui.Ui;
import vince.exception.VinceException;

/**
 * Finds tasks whose descriptions contain a given keyword (case-insensitive).
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        List<String> lines = tasks.findTasks(keyword);
        ui.showTaskList(lines);
    }

    public String getKeyword() {
        return keyword;
    }
}
