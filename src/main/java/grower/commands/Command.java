package grower.commands;

import grower.tasks.TaskList;
import grower.ui.Ui;
import grower.growerExceptions.GrowerException;

/**
 * Commands package is for all user grower.commands. All classes should inherit from the Command abstract class
 * and implement the execute method
 */

public abstract class Command {
    /**
     * Executes this command using the application's task data and user interface.
     *
     * @param tasks task list to read or modify
     * @param ui user interface used to display the command result
     * @return true if the application should continue accepting commands
     * @throws GrowerException if the command cannot be completed
     */
    public abstract boolean execute(TaskList tasks, Ui ui) throws GrowerException;
}
