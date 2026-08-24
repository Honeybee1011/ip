package grower.commands;

import grower.Grower;

public class MarkCommand extends Command{
    private int index;

    public MarkCommand(int index) {
        this.index = index;
    }
    @Override
    public boolean execute() {
        Grower.taskList.markTask(index);
        return true;
    }
}

