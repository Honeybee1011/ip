package grower.commands;

import grower.Grower;
import grower.tasks.Task;
import grower.tasks.Event;

public class EventCommand extends Command {
    private String description;
    private String start;
    private String end;

    public EventCommand(String description, String start, String end) {
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