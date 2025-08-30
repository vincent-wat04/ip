public class Parser {
    public static Command parse(String input) {
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
            case "on": {
                String dateStr = trimmed.length() > 3 ? trimmed.substring(3).trim() : "";
                return new OnDateCommand(dateStr);
            }
            case "todo":
            case "deadline":
            case "event":
                return new AddCommand(trimmed);
            default:
                return null;
        }
    }
}
