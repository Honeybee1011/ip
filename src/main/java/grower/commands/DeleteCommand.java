package grower.commands;

/**
 * Represents a user command to delete an item from the list
 */

import grower.Grower;

public class DeleteCommand extends Command {
    private int index;

    public DeleteCommand(String index) {
        this.index = Integer.parseInt(index);
    }

    @Override
    public boolean execute() {
        Grower.taskList.deleteTask(index - 1);
        return true;
    }
}