/**
 * Represents a task and whether it has been completed.
 */

public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void mark() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns a displayable representation of this task.
     *
     * @return task status and description
     */
    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        return "[" + status + "] " + description;
    }
}