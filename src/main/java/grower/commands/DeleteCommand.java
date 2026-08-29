package grower.commands;

import grower.TaskList;
import grower.Ui;
import grower.growerExceptions.InvalidTaskNumberException;
import grower.tasks.Task;

/**
 * Represents a user command to delete an item from the list.
 */
public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui) throws InvalidTaskNumberException {
        Task deletedTask = tasks.deleteTask(index);
        ui.showTaskDeleted(deletedTask);
        return true;
    }
}
