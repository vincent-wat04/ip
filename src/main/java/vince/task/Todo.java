package vince.task;

import vince.exception.VinceException;

/**
 * A simple to-do task with only a description.
 */
public class Todo extends Task {
    /**
     * Creates a to-do task.
     * @param description description of the task
     * @throws VinceException if description is invalid
     */
    public Todo(String description) throws VinceException {
        super(description);
    }

    public String toString() {
        return "[T] " + super.toString();
    }
}
