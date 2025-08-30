package vince.task;

import vince.exception.VinceException;

public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) throws VinceException {
        if (description == null || description.trim().isEmpty()) {
            throw new VinceException("Task description cannot be null or empty!");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}
