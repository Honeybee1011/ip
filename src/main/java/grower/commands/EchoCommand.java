package grower.commands;

import grower.TaskList;
import grower.Ui;

public class EchoCommand extends Command {
    private final String textToEcho;

    public EchoCommand(String textToEcho) {
        this.textToEcho = textToEcho;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        ui.showMessage(this.textToEcho);
        return true;
    }
}
