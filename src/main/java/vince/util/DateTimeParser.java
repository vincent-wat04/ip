package vince.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import vince.exception.VinceException;

/**
 * Utility for parsing and formatting dates/times used in tasks.
 * Supports multiple input formats and produces English-formatted outputs.
 */
public class DateTimeParser {
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4})\\s+(\\d{4})");
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4})");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{4})");
    
    /**
     * Parses a date/time string into a LocalDateTime.
     * Accepted formats include: yyyy-MM-dd, dd/MM/yyyy HHmm, dd/MM/yyyy, HHmm.
     * @param dateTimeStr input string
     * @return parsed LocalDateTime
     * @throws VinceException if parsing fails or input is blank
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) throws VinceException {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new VinceException("Date/time string cannot be empty!");
        }
        
        String input = dateTimeStr.trim();
        assert input != null && !input.isEmpty() : "Input should not be null or empty after trimming";
        
        // Handle natural language dates first
        LocalDateTime naturalDate = parseNaturalLanguageDate(input.toLowerCase());
        if (naturalDate != null) {
            assert naturalDate != null : "Parsed natural language date should not be null";
            return naturalDate;
        }
        
        // Try yyyy-mm-dd format first
        try {
            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime result = date.atStartOfDay();
            assert result != null : "Parsed LocalDateTime should not be null";
            return result;
        } catch (DateTimeParseException e) {
            // Continue to other formats
        }
        
        // Try dd/mm/yyyy HHMM format
        Matcher dateTimeMatcher = DATE_TIME_PATTERN.matcher(input);
        if (dateTimeMatcher.matches()) {
            try {
                String dateStr = dateTimeMatcher.group(1);
                String timeStr = dateTimeMatcher.group(2);
                assert dateStr != null && timeStr != null : "Matched groups should not be null";
                
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HHmm"));
                
                LocalDateTime result = LocalDateTime.of(date, time);
                assert result != null : "Parsed LocalDateTime should not be null";
                return result;
            } catch (DateTimeParseException e) {
                throw new VinceException("Invalid date/time format: " + input + ". Use dd/mm/yyyy HHMM or yyyy-mm-dd");
            }
        }
        
        // Try dd/mm/yyyy format (date only)
        Matcher dateMatcher = DATE_PATTERN.matcher(input);
        if (dateMatcher.matches()) {
            try {
                LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return date.atStartOfDay();
            } catch (DateTimeParseException e) {
                throw new VinceException("Invalid date format: " + input + ". Use dd/mm/yyyy");
            }
        }
        
        // Try HHMM format (time only, assume today)
        Matcher timeMatcher = TIME_PATTERN.matcher(input);
        if (timeMatcher.matches()) {
            try {
                LocalTime time = LocalTime.parse(input, DateTimeFormatter.ofPattern("HHmm"));
                return LocalDateTime.of(LocalDate.now(), time);
            } catch (DateTimeParseException e) {
                throw new VinceException("Invalid time format: " + input + ". Use HHMM");
            }
        }
        
        throw new VinceException("Unsupported date/time format: " + input + 
            ". Supported formats: yyyy-mm-dd, dd/mm/yyyy HHMM, dd/mm/yyyy, HHMM");
    }
    
    /**
     * Formats a date/time as an English string, e.g. "Dec 15 2024, 18:00".
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm", Locale.ENGLISH));
    }
    
    /** Formats a date as an English string, e.g. "Dec 15 2024". */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH));
    }
    
    /** Formats a time as 24-hour HH:mm. */
    public static String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH));
    }
    
    /**
     * AI-enhanced natural language date parsing.
     * Supports common expressions like "today", "tomorrow", "next week", etc.
     * 
     * @param input the natural language input (should be lowercase)
     * @return parsed LocalDateTime or null if not recognized
     */
    private static LocalDateTime parseNaturalLanguageDate(String input) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        
        // Handle relative dates
        switch (input) {
            case "today":
                return today.atStartOfDay();
            case "tomorrow":
                return today.plusDays(1).atStartOfDay();
            case "yesterday":
                return today.minusDays(1).atStartOfDay();
            case "next week":
                return today.plusWeeks(1).atStartOfDay();
            case "next month":
                return today.plusMonths(1).atStartOfDay();
        }
        
        // Handle "in X days/weeks/months"
        if (input.startsWith("in ") && input.contains(" ")) {
            String[] parts = input.split(" ");
            if (parts.length >= 3) {
                try {
                    int amount = Integer.parseInt(parts[1]);
                    String unit = parts[2];
                    
                    switch (unit) {
                        case "day":
                        case "days":
                            return today.plusDays(amount).atStartOfDay();
                        case "week":
                        case "weeks":
                            return today.plusWeeks(amount).atStartOfDay();
                        case "month":
                        case "months":
                            return today.plusMonths(1).atStartOfDay();
                    }
                } catch (NumberFormatException e) {
                    // Continue to other parsing methods
                }
            }
        }
        
        // Handle "next monday", "this friday", etc.
        if (input.startsWith("next ") || input.startsWith("this ")) {
            String[] parts = input.split(" ");
            if (parts.length == 2) {
                String dayName = parts[1];
                int targetDayOfWeek = getDayOfWeekFromName(dayName);
                if (targetDayOfWeek != -1) {
                    boolean isNext = parts[0].equals("next");
                    return getNextOccurrenceOfDay(today, targetDayOfWeek, isNext);
                }
            }
        }
        
        // Handle time expressions like "today 3pm", "tomorrow 1400"
        if (input.contains(" ")) {
            String[] parts = input.split(" ");
            if (parts.length == 2) {
                LocalDateTime baseDate = parseNaturalLanguageDate(parts[0]);
                if (baseDate != null) {
                    LocalTime time = parseNaturalLanguageTime(parts[1]);
                    if (time != null) {
                        return baseDate.with(time);
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Parses natural language time expressions.
     */
    private static LocalTime parseNaturalLanguageTime(String timeStr) {
        timeStr = timeStr.toLowerCase();
        
        // Handle AM/PM format
        if (timeStr.endsWith("am") || timeStr.endsWith("pm")) {
            boolean isPM = timeStr.endsWith("pm");
            String numPart = timeStr.substring(0, timeStr.length() - 2);
            
            try {
                int hour;
                int minute = 0;
                
                if (numPart.contains(":")) {
                    String[] timeParts = numPart.split(":");
                    hour = Integer.parseInt(timeParts[0]);
                    minute = Integer.parseInt(timeParts[1]);
                } else {
                    hour = Integer.parseInt(numPart);
                }
                
                if (isPM && hour != 12) {
                    hour += 12;
                } else if (!isPM && hour == 12) {
                    hour = 0;
                }
                
                return LocalTime.of(hour, minute);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        // Handle 24-hour format (HHMM)
        if (timeStr.matches("\\d{3,4}")) {
            try {
                int timeInt = Integer.parseInt(timeStr);
                int hour = timeInt / 100;
                int minute = timeInt % 100;
                return LocalTime.of(hour, minute);
            } catch (Exception e) {
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Gets the next occurrence of a specific day of week.
     */
    private static LocalDateTime getNextOccurrenceOfDay(LocalDate from, int targetDayOfWeek, boolean forceNext) {
        int currentDayOfWeek = from.getDayOfWeek().getValue();
        int daysToAdd;
        
        if (forceNext || targetDayOfWeek <= currentDayOfWeek) {
            daysToAdd = 7 - currentDayOfWeek + targetDayOfWeek;
        } else {
            daysToAdd = targetDayOfWeek - currentDayOfWeek;
        }
        
        return from.plusDays(daysToAdd).atStartOfDay();
    }
    
    /**
     * Converts day name to day of week number (1=Monday, 7=Sunday).
     */
    private static int getDayOfWeekFromName(String dayName) {
        switch (dayName.toLowerCase()) {
            case "monday":
            case "mon":
                return 1;
            case "tuesday":
            case "tue":
                return 2;
            case "wednesday":
            case "wed":
                return 3;
            case "thursday":
            case "thu":
                return 4;
            case "friday":
            case "fri":
                return 5;
            case "saturday":
            case "sat":
                return 6;
            case "sunday":
            case "sun":
                return 7;
            default:
                return -1;
        }
    }
}
