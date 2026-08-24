package grower.commands;

import grower.Grower;
import grower.tasks.Task;
import grower.tasks.ToDo;

public class ToDoCommand extends Command {
    private String description;

    public ToDoCommand(String description) {
        this.description = description;
    }

    @Override
    public boolean execute() {
        // Create a new Task object from the description.
        Task newTask = new ToDo(this.description);
        // Use the static taskList from the Grower class to add the new task.
        Grower.taskList.addTask(newTask);
        // Return true to indicate that the application should continue running.
        return true;
    }
}