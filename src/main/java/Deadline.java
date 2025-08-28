public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) throws VinceException {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new VinceException("Deadline date cannot be null or empty!");
        }
        this.by = by.trim();
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + by + ")";
    }
}
