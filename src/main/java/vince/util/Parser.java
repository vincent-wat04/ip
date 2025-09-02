package vince.util;

import vince.command.Command;
import vince.command.ExitCommand;
import vince.command.ListCommand;
import vince.command.MarkCommand;
import vince.command.UnmarkCommand;
import vince.command.DeleteCommand;
import vince.command.OnDateCommand;
import vince.command.AddCommand;
import vince.command.FindCommand;
import vince.exception.VinceException;

/**
 * Parses raw user input into executable {@link Command} instances.
 */
public class Parser {
    /**
     * Parses a command line into a {@link Command}.
     * @param input raw input line
     * @return a concrete command instance
     * @throws VinceException if the command is not recognized
     */
    public static Command parse(String input) throws VinceException {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String trimmed = input.trim();
        String[] parts = trimmed.split(" ");
        String head = parts[0];
        switch (head) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand();
            case "mark":
                return new MarkCommand(parts.length > 1 ? parts[1] : "");
            case "unmark":
                return new UnmarkCommand(parts.length > 1 ? parts[1] : "");
            case "delete":
                return new DeleteCommand(parts.length > 1 ? parts[1] : "");
            case "find": {
                String keyword = trimmed.length() > 4 ? trimmed.substring(4).trim() : "";
                return new FindCommand(keyword);
            }
            case "on": {
                String dateStr = trimmed.length() > 3 ? trimmed.substring(3).trim() : "";
                return new OnDateCommand(dateStr);
            }
            case "todo":
            case "deadline":
            case "event":
                return new AddCommand(trimmed);
            default:
                throw new VinceException("I'm sorry, but I don't know what that means :-(");
        }
    }
}
