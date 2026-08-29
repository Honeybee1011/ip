package grower.commands;

import grower.TaskList;
import grower.Ui;
import grower.growerExceptions.InvalidTaskNumberException;
import grower.tasks.Task;

public class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }
    @Override
    public boolean execute(TaskList tasks, Ui ui) throws InvalidTaskNumberException {
        Task unmarkedTask = tasks.unmarkTask(index);
        ui.showTaskUnmarked(unmarkedTask);
        return true;
    }
}
