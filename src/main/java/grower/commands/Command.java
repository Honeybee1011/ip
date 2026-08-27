package grower.commands;

import grower.growerExceptions.GrowerException;

/**
 * Commands package is for all user grower.commands. All classes should inherit from the Command abstract class
 * and implement the execute method
 */

public abstract class Command {
    public abstract boolean execute() throws GrowerException;
}
