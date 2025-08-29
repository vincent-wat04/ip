import java.time.LocalDateTime;

public class Deadline extends Task {
    private LocalDateTime by;

    public Deadline(String description, String by) throws VinceException {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new VinceException("Deadline date cannot be null or empty!");
        }
        this.by = DateTimeParser.parseDateTime(by.trim());
    }

    public Deadline(String description, LocalDateTime by) throws VinceException {
        super(description);
        if (by == null) {
            throw new VinceException("Deadline date cannot be null!");
        }
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    public String getByString() {
        return DateTimeParser.formatDateTime(by);
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + DateTimeParser.formatDateTime(by) + ")";
    }
}
