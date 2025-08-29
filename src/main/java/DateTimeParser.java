import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeParser {
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4})\\s+(\\d{4})");
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4})");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{4})");
    
    public static LocalDateTime parseDateTime(String dateTimeStr) throws VinceException {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new VinceException("Date/time string cannot be empty!");
        }
        
        String input = dateTimeStr.trim();
        
        // Try yyyy-mm-dd format first
        try {
            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.atStartOfDay();
        } catch (DateTimeParseException e) {
            // Continue to other formats
        }
        
        // Try dd/mm/yyyy HHMM format
        Matcher dateTimeMatcher = DATE_TIME_PATTERN.matcher(input);
        if (dateTimeMatcher.matches()) {
            try {
                String dateStr = dateTimeMatcher.group(1);
                String timeStr = dateTimeMatcher.group(2);
                
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HHmm"));
                
                return LocalDateTime.of(date, time);
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
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm", Locale.ENGLISH));
    }
    
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH));
    }
    
    public static String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH));
    }
}
