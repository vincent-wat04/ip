public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) throws VinceException {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new VinceException("Event start time cannot be null or empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new VinceException("Event end time cannot be null or empty!");
        }
        this.from = from.trim();
        this.to = to.trim();
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + from + " to: " + to + ")";
    }

}
