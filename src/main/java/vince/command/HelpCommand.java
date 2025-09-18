package vince.command;

import java.util.List;
import vince.storage.TaskList;
import vince.ui.Ui;
import vince.ai.TaskSuggestionEngine;
import vince.exception.VinceException;

/**
 * AI-enhanced help command that provides contextual assistance.
 */
public class HelpCommand extends Command {
    
    @Override
    public void execute(TaskList tasks, Ui ui) throws VinceException {
        ui.showHelp();
        
        // Show AI-powered suggestions based on current context
        List<String> suggestions = TaskSuggestionEngine.generateSuggestions(tasks.getAllTasks());
        if (!suggestions.isEmpty()) {
            ui.showSuggestions(suggestions);
        }
    }
}
