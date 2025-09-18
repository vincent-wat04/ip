package vince.task;

import vince.exception.VinceException;

/**
 * Represents a user task with description, completion state, and AI-enhanced priority.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected Priority priority;

    /**
     * Constructs a task with the given description and auto-suggested priority.
     * @param description non-empty description string
     * @throws VinceException if the description is null or blank
     */
    public Task(String description) throws VinceException {
        if (description == null || description.trim().isEmpty()) {
            throw new VinceException("Task description cannot be null or empty!");
        }
        this.description = description.trim();
        this.isDone = false;
        this.priority = Priority.suggestPriority(this.description);
        assert this.description != null && !this.description.isEmpty() : "Description should not be null or empty after construction";
        assert !this.isDone : "New task should not be marked as done initially";
        assert this.priority != null : "Priority should be assigned";
    }
    
    /**
     * Constructs a task with specified description and priority.
     * @param description non-empty description string
     * @param priority the task priority
     * @throws VinceException if the description is null or blank
     */
    public Task(String description, Priority priority) throws VinceException {
        if (description == null || description.trim().isEmpty()) {
            throw new VinceException("Task description cannot be null or empty!");
        }
        this.description = description.trim();
        this.isDone = false;
        this.priority = priority != null ? priority : Priority.NONE;
        assert this.description != null && !this.description.isEmpty() : "Description should not be null or empty after construction";
        assert !this.isDone : "New task should not be marked as done initially";
        assert this.priority != null : "Priority should be assigned";
    }

    /** Marks the task as done. */
    public void mark() {
        assert !this.isDone : "Task should not already be marked as done";
        this.isDone = true;
        assert this.isDone : "Task should be marked as done after mark() call";
    }

    /** Marks the task as not done. */
    public void unmark() {
        assert this.isDone : "Task should be marked as done before unmarking";
        this.isDone = false;
        assert !this.isDone : "Task should not be marked as done after unmark() call";
    }

    /**
     * Returns whether this task is completed.
     * @return true if completed, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task description.
     * @return description string
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the task priority.
     * @return priority level
     */
    public Priority getPriority() {
        return priority;
    }
    
    /**
     * Sets the task priority.
     * @param priority the new priority level
     */
    public void setPriority(Priority priority) {
        this.priority = priority != null ? priority : Priority.NONE;
        assert this.priority != null : "Priority should not be null after setting";
    }

    /**
     * Returns a string with priority, completion indicator and description.
     */
    public String toString() {
        String statusIcon = isDone ? "[X] " : "[ ] ";
        String priorityIcon = priority != Priority.NONE ? priority.getEmoji() + " " : "";
        return priorityIcon + statusIcon + description;
    }
}
