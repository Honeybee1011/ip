package grower.commands;

import java.time.LocalDateTime;

import grower.Grower;
import grower.tasks.Deadline;
import grower.tasks.Task;

public class DeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime deadline;

    public DeadlineCommand(String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    @Override
    public boolean execute() {
        // Create a new Task object from the description.
        Task newTask = new Deadline(this.description, this.deadline);
        // Use the static taskList from the Grower class to add the new task.
        Grower.taskList.addTask(newTask);
        // Return true to indicate that the application should continue running.
        return true;
    }
}
