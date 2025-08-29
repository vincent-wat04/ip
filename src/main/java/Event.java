import java.time.LocalDateTime;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

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

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromString() {
        return DateTimeParser.formatDateTime(from);
    }

    public String getToString() {
        return DateTimeParser.formatDateTime(to);
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + DateTimeParser.formatDateTime(from) + " to: " + DateTimeParser.formatDateTime(to) + ")";
    }

}
