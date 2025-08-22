public enum TaskType {
    TODO("todo", 5),
    DEADLINE("deadline", 9),
    EVENT("event", 6);
    
    private final String command;
    private final int prefixLength;
    
    TaskType(String command, int prefixLength) {
        this.command = command;
        this.prefixLength = prefixLength;
    }
    
    public String getCommand() {
        return command;
    }
    
    public int getPrefixLength() {
        return prefixLength;
    }
    
    public static TaskType fromCommand(String command) {
        for (TaskType type : values()) {
            if (type.command.equals(command)) {
                return type;
            }
        }
        return null;
    }
}
