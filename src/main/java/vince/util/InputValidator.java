package vince.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import vince.exception.VinceException;

/**
 * AI-enhanced input validation and suggestion system.
 * Provides intelligent error messages and command suggestions.
 */
public class InputValidator {
    
    // Common typos and their corrections
    private static final String[][] COMMAND_CORRECTIONS = {
        {"list", "lst", "lsit", "lis"},
        {"todo", "tdo", "tod", "to-do"},
        {"deadline", "deadlin", "dedline", "dead-line"},
        {"event", "evnt", "evetn", "ev"},
        {"mark", "mrk", "mak"},
        {"unmark", "unmrk", "unm"},
        {"delete", "del", "delet", "remove", "rm"},
        {"find", "fnd", "search", "look"},
        {"schedule", "sched", "schedul", "timetable"},
        {"bye", "exit", "quit", "close"}
    };
    
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "\\d{1,2}[/-]\\d{1,2}[/-]\\d{4}|\\d{4}[/-]\\d{1,2}[/-]\\d{1,2}|today|tomorrow|yesterday"
    );
    
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "\\d{1,2}:?\\d{2}|\\d{1,2}\\s*(am|pm|AM|PM)"
    );
    
    /**
     * Validates and suggests corrections for user input.
     * 
     * @param input the raw user input
     * @return validation result with suggestions
     */
    public static ValidationResult validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ValidationResult(false, "Command cannot be empty!", 
                List.of("Try: 'list' to see all tasks", "'help' for available commands"));
        }
        
        String trimmed = input.trim().toLowerCase();
        String[] parts = trimmed.split("\\s+");
        String command = parts[0];
        
        // Check for typos and suggest corrections
        String correctedCommand = findCorrection(command);
        if (correctedCommand != null && !correctedCommand.equals(command)) {
            return new ValidationResult(false, 
                String.format("Did you mean '%s'?", correctedCommand),
                List.of(String.format("Try: '%s%s'", correctedCommand, 
                    input.length() > command.length() ? input.substring(command.length()) : "")));
        }
        
        // Validate specific command formats
        return validateCommandFormat(parts, input);
    }
    
    /**
     * Finds the best correction for a potentially misspelled command.
     */
    private static String findCorrection(String input) {
        for (String[] corrections : COMMAND_CORRECTIONS) {
            String correct = corrections[0];
            for (int i = 1; i < corrections.length; i++) {
                if (corrections[i].equals(input) || 
                    levenshteinDistance(input, corrections[i]) <= 1) {
                    return correct;
                }
            }
            // Also check similarity to the correct command
            if (levenshteinDistance(input, correct) <= 2) {
                return correct;
            }
        }
        return null;
    }
    
    /**
     * Validates command-specific format requirements.
     */
    private static ValidationResult validateCommandFormat(String[] parts, String input) {
        String command = parts[0];
        List<String> suggestions = new ArrayList<>();
        
        switch (command) {
            case "deadline":
                if (!input.contains(" /by ")) {
                    suggestions.add("deadline <description> /by <date>");
                    suggestions.add("Example: deadline Submit report /by 15/12/2024 1700");
                    return new ValidationResult(false, 
                        "Deadline tasks need a '/by' clause to specify the deadline!", suggestions);
                }
                break;
                
            case "event":
                if (!input.contains(" /from ") || !input.contains(" /to ")) {
                    suggestions.add("event <description> /from <start> /to <end>");
                    suggestions.add("Example: event Team meeting /from 15/12/2024 1000 /to 15/12/2024 1100");
                    return new ValidationResult(false, 
                        "Event tasks need both '/from' and '/to' clauses!", suggestions);
                }
                break;
                
            case "mark":
            case "unmark":
            case "delete":
                if (parts.length < 2) {
                    suggestions.add(String.format("%s <task_number>", command));
                    suggestions.add("Example: " + command + " 1");
                    return new ValidationResult(false, 
                        String.format("Please specify which task to %s!", command), suggestions);
                }
                try {
                    Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    suggestions.add(String.format("%s <task_number>", command));
                    suggestions.add("Task number must be a positive integer");
                    return new ValidationResult(false, 
                        "Task number must be a valid number!", suggestions);
                }
                break;
                
            case "find":
                if (parts.length < 2) {
                    suggestions.add("find <keyword>");
                    suggestions.add("Example: find meeting");
                    return new ValidationResult(false, 
                        "Please specify what to search for!", suggestions);
                }
                break;
                
            case "schedule":
            case "on":
                if (parts.length < 2) {
                    suggestions.add(command + " <date>");
                    suggestions.add("Example: " + command + " 15/12/2024");
                    suggestions.add("Example: " + command + " today");
                    return new ValidationResult(false, 
                        "Please specify a date!", suggestions);
                }
                break;
        }
        
        return new ValidationResult(true, "Valid command", suggestions);
    }
    
    /**
     * Calculates Levenshtein distance between two strings for fuzzy matching.
     */
    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Generates helpful suggestions based on current task list state.
     */
    public static List<String> generateContextualSuggestions(int taskCount, boolean hasDeadlines) {
        List<String> suggestions = new ArrayList<>();
        
        if (taskCount == 0) {
            suggestions.add("💡 Start by adding your first task: 'todo <description>'");
            suggestions.add("💡 Or add a deadline: 'deadline <description> /by <date>'");
        } else {
            suggestions.add("💡 Type 'list' to see all your tasks");
            if (hasDeadlines) {
                suggestions.add("💡 Check today's schedule: 'schedule today'");
            }
            suggestions.add("💡 Search tasks: 'find <keyword>'");
        }
        
        return suggestions;
    }
    
    /**
     * Result of input validation with error message and suggestions.
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final String message;
        private final List<String> suggestions;
        
        public ValidationResult(boolean isValid, String message, List<String> suggestions) {
            this.isValid = isValid;
            this.message = message;
            this.suggestions = suggestions != null ? suggestions : new ArrayList<>();
        }
        
        public boolean isValid() {
            return isValid;
        }
        
        public String getMessage() {
            return message;
        }
        
        public List<String> getSuggestions() {
            return suggestions;
        }
        
        public boolean hasSuggestions() {
            return !suggestions.isEmpty();
        }
    }
}
