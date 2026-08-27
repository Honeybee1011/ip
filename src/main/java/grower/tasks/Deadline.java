package grower.tasks;

public class Deadline extends Task {
    String deadline;

    public Deadline(String description, String deadline){
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toFileString() {
        return String.format(
                "D | %d | %s | %s",
                isCompleted() ? 1 : 0,
                getDescription(),
                this.deadline
        );
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), deadline);
    }
}