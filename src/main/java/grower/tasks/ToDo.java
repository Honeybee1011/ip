package grower.tasks;

public class ToDo extends Task {
    public ToDo(String description){
        super(description);
    }


    @Override
    public String toFileString() {
        return String.format(
                "T | %d | %s",
                isCompleted() ? 1 : 0,
                getDescription()
        );
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}