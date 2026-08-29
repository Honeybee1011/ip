package grower.commands;

import grower.tasks.TaskList;
import grower.ui.Ui;

public class ListCommand extends Command {
    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        ui.showTaskList(tasks.getTasks());
        return true;
    }
}
