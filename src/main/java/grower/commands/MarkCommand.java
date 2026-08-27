package grower.commands;

import grower.Grower;
import grower.growerExceptions.InvalidTaskNumberException;

public class MarkCommand extends Command{
    private int index;

    public MarkCommand(int index) {
        this.index = index;
    }
    @Override
    public boolean execute() throws InvalidTaskNumberException {
        Grower.taskList.markTask(index);
        System.out.println("Marking following task as done!");
        Grower.taskList.printTask(index);
        return true;
    }
}
