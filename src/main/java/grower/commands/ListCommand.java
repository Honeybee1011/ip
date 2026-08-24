package grower.commands;

import grower.Grower;

public class ListCommand extends Command {
    @Override
    public boolean execute() {
        Grower.taskList.printTask();
        return true;
    }
}