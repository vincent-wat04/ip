package vince.task;

import vince.exception.VinceException;

public class Todo extends Task {
    public Todo(String description) throws VinceException {
        super(description);
    }

    public String toString() {
        return "[T] " + super.toString();
    }
}
