package vince.task;

import java.time.LocalDateTime;
import vince.exception.VinceException;
import vince.util.DateTimeParser;

/**
 * A task with a deadline represented as a LocalDateTime.
 */
public class Deadline extends Task {
    private LocalDateTime by;

    /**
     * Creates a deadline task, parsing the date/time from a string.
     * @param description task description
     * @param by string date/time accepted by {@link DateTimeParser}
     * @throws VinceException if parameters are invalid or parse fails
     */
    public Deadline(String description, String by) throws VinceException {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new VinceException("Deadline date cannot be null or empty!");
        }
        this.by = DateTimeParser.parseDateTime(by.trim());
    }

    /**
     * Creates a deadline task with a concrete LocalDateTime.
     * @param description task description
     * @param by LocalDateTime value of the deadline
     * @throws VinceException if the deadline is null
     */
    public Deadline(String description, LocalDateTime by) throws VinceException {
        super(description);
        if (by == null) {
            throw new VinceException("Deadline date cannot be null!");
        }
        this.by = by;
    }

    /**
     * Returns the deadline timestamp.
     * @return LocalDateTime deadline
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the formatted deadline timestamp.
     * @return e.g., "Dec 15 2024, 18:00"
     */
    public String getByString() {
        return DateTimeParser.formatDateTime(by);
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + DateTimeParser.formatDateTime(by) + ")";
    }
}
