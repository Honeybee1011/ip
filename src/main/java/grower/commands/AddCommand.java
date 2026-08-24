package grower.commands;

import grower.Grower;
import grower.Task;

/**
 * Represents a command to add a new task to the task list.
 */
public class AddCommand extends Command {
    private final String description;

    /**
     * Creates an AddCommand.
     *
     * @param description The description of the task to add.
     */
    public AddCommand (String description) {
        this.description = description;
    }

    @Override
    public boolean execute() {
        // Create a new Task object from the description.
        Task newTask = new Task(this.description);
        // Use the static taskList from the Grower class to add the new task.
        Grower.taskList.addTask(newTask);
        // Return true to indicate that the application should continue running.
        return true; 
    }
}