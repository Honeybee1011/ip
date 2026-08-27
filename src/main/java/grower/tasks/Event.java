package grower.tasks;

public class Event extends Task {
    String start;
    String end;

    public Event(String description, String start, String end){
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toFileString() {
        return String.format(
                "E | %d | %s | %s | %s",
                isCompleted() ? 1 : 0,
                getDescription(),
                this.start,
                this.end
        );
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), start, end);
    }
}