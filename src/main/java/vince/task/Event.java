package vince.task;

import java.time.LocalDateTime;
import vince.exception.VinceException;
import vince.util.DateTimeParser;

/**
 * A time-ranged event task with a start (from) and end (to) timestamp.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Creates an event by parsing the start/end date/time from strings.
     * @param description task description
     * @param from start date/time accepted by {@link DateTimeParser}
     * @param to end date/time accepted by {@link DateTimeParser}
     * @throws VinceException if inputs are invalid or parse fails
     */
    public Event(String description, String from, String to) throws VinceException {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new VinceException("Event start time cannot be null or empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new VinceException("Event end time cannot be empty!");
        }
        this.from = DateTimeParser.parseDateTime(from.trim());
        this.to = DateTimeParser.parseDateTime(to.trim());
    }

    /**
     * Creates an event from concrete start and end timestamps.
     * @param description task description
     * @param from start timestamp
     * @param to end timestamp
     * @throws VinceException if any timestamp is null
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) throws VinceException {
        super(description);
        if (from == null) {
            throw new VinceException("Event start time cannot be null!");
        }
        if (to == null) {
            throw new VinceException("Event end time cannot be null!");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start timestamp.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end timestamp.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /** Returns formatted start timestamp. */
    public String getFromString() {
        return DateTimeParser.formatDateTime(from);
    }

    /** Returns formatted end timestamp. */
    public String getToString() {
        return DateTimeParser.formatDateTime(to);
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + DateTimeParser.formatDateTime(from) + " to: " + DateTimeParser.formatDateTime(to) + ")";
    }

}
