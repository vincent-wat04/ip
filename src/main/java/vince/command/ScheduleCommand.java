package vince.command;

import java.util.List;
import vince.storage.TaskList;
import vince.ui.Ui;
import vince.exception.VinceException;

/**
 * Command to display a schedule view of tasks for a specific date.
 * Shows tasks organized by time with a timeline format.
 */
public class ScheduleCommand extends Command {
    private final String dateStr;

    public ScheduleCommand(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        String dateLabel = tasks.tasksOnDateLabel(dateStr);
        List<String> scheduleLines = tasks.getScheduleForDate(dateStr);
        ui.showSchedule(dateLabel, scheduleLines);
    }

    public String getDateStr() {
        return dateStr;
    }
}
