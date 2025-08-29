public enum Command {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    ON("on"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event");
    
    private final String commandString;
    
    Command(String commandString) {
        this.commandString = commandString;
    }
    
    public String getCommandString() {
        return commandString;
    }
    
    public static Command fromString(String commandString) {
        for (Command command : values()) {
            if (command.commandString.equals(commandString)) {
                return command;
            }
        }
        return null;
    }
}
