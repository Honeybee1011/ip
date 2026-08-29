package grower.commands;

import grower.TaskList;
import grower.Ui;

public class ByeCommand extends Command {
    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        ui.showGoodbye();
        return false;
    }
}
