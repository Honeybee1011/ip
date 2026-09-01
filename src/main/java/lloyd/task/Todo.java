package lloyd.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete task with no associated date or time.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a displayable representation prefixed with the todo type marker.
     *
     * @return todo type, completion status, and description
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
