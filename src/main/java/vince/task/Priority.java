package vince.task;

/**
 * AI-enhanced task priority system.
 * Provides intelligent priority assignment and display.
 */
public enum Priority {
    HIGH("🔴", "HIGH", 3),
    MEDIUM("🟡", "MED", 2),
    LOW("🟢", "LOW", 1),
    NONE("⚪", "", 0);
    
    private final String emoji;
    private final String shortName;
    private final int value;
    
    Priority(String emoji, String shortName, int value) {
        this.emoji = emoji;
        this.shortName = shortName;
        this.value = value;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * Parses priority from string input.
     * Supports various formats: "high", "h", "1", "🔴", etc.
     */
    public static Priority fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return NONE;
        }
        
        String normalized = input.trim().toLowerCase();
        
        // Handle emoji input
        switch (normalized) {
            case "🔴":
                return HIGH;
            case "🟡":
                return MEDIUM;
            case "🟢":
                return LOW;
            case "⚪":
                return NONE;
        }
        
        // Handle text input
        switch (normalized) {
            case "high":
            case "h":
            case "urgent":
            case "important":
                return HIGH;
            case "medium":
            case "med":
            case "m":
            case "normal":
                return MEDIUM;
            case "low":
            case "l":
            case "minor":
                return LOW;
            case "none":
            case "n":
            case "":
                return NONE;
        }
        
        // Handle numeric input
        try {
            int num = Integer.parseInt(normalized);
            switch (num) {
                case 3:
                    return HIGH;
                case 2:
                    return MEDIUM;
                case 1:
                    return LOW;
                case 0:
                default:
                    return NONE;
            }
        } catch (NumberFormatException e) {
            return NONE;
        }
    }
    
    /**
     * AI-enhanced priority suggestion based on task content.
     * Analyzes task description for urgency indicators.
     */
    public static Priority suggestPriority(String description) {
        if (description == null || description.trim().isEmpty()) {
            return NONE;
        }
        
        String lower = description.toLowerCase();
        
        // High priority keywords
        if (containsAny(lower, "urgent", "asap", "emergency", "critical", "deadline", 
                       "important", "meeting", "interview", "exam", "presentation")) {
            return HIGH;
        }
        
        // Medium priority keywords
        if (containsAny(lower, "soon", "report", "project", "assignment", "call", 
                       "email", "review", "plan")) {
            return MEDIUM;
        }
        
        // Low priority keywords
        if (containsAny(lower, "someday", "maybe", "consider", "think about", 
                       "when free", "leisure", "hobby")) {
            return LOW;
        }
        
        return NONE;
    }
    
    private static boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return emoji + (shortName.isEmpty() ? "" : " " + shortName);
    }
}
