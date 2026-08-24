package grower.commands;

import grower.Grower;

public class UnmarkCommand extends Command{
    private int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }
    @Override
    public boolean execute() {
        Grower.taskList.unmarkTask(index);
        System.out.println("Marking following task as not done :(");
        Grower.taskList.printTask(index);
        return true;
    }
}