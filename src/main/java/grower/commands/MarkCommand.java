package grower.commands;

import grower.tasks.TaskList;
import grower.ui.Ui;
import grower.growerExceptions.InvalidTaskNumberException;
import grower.tasks.Task;

public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }
    @Override
    public boolean execute(TaskList tasks, Ui ui) throws InvalidTaskNumberException {
        Task markedTask = tasks.markTask(index);
        ui.showTaskMarked(markedTask);
        return true;
    }
}
