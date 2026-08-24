package grower.commands;

import grower.Grower;
import grower.tasks.Task;
import grower.tasks.Deadline;

public class DeadlineCommand extends Command {
    private String description;
    private String deadline;

    public DeadlineCommand(String description, String deadline) {
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