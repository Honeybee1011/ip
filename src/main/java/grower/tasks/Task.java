package grower.tasks;

/**
 * Task is an abstract class for tasks. Classes inheriting should pass the task description up to task.
 */

//Task should never be initialized
public abstract class Task {
    //Description of task
    private String description;

    //Status of task
    private boolean completed;

    public Task(String description) {
        this.description = description;
        this.completed = false;
    }

    public void unmark() {
        this.completed = false;
    }

    public void mark() {
        this.completed = true;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * A fixed method for reliably writing tasks to a file
     * @return String representing the task for writing to a file
     */
    public abstract String toFileString();

    @Override
    public String toString() {
        String tick;
        if (completed) {
            tick = "X";
        } else {
            tick = " ";
        }

        return String.format("[%s] %s", tick, this.description);
    }
}