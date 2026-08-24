package grower;

public class Task {
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