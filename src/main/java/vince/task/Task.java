package vince.task;

import vince.exception.VinceException;

/**
 * Represents a user task with a description and completion state.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructs a task with the given description.
     * @param description non-empty description string
     * @throws VinceException if the description is null or blank
     */
    public Task(String description) throws VinceException {
        if (description == null || description.trim().isEmpty()) {
            throw new VinceException("Task description cannot be null or empty!");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    /** Marks the task as done. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks the task as not done. */
    public void unmark() {
        this.isDone = false;
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
     * Returns a string with the completion indicator and description.
     */
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}
