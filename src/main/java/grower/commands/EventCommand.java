package grower.commands;

import java.time.LocalDateTime;

import grower.Grower;
import grower.tasks.Event;
import grower.tasks.Task;

public class EventCommand extends Command {
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public EventCommand(String description, LocalDateTime start, LocalDateTime end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean execute() {
        // Create a new Task object from the description.
        Task newTask = new Event(this.description, this.start, this.end);
        // Use the static taskList from the Grower class to add the new task.
        Grower.taskList.addTask(newTask);
        // Return true to indicate that the application should continue running.
        return true;
    }
}
