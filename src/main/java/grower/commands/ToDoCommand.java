package grower.commands;

import grower.TaskList;
import grower.Ui;
import grower.tasks.Task;
import grower.tasks.ToDo;

public class ToDoCommand extends Command {
    private final String description;

    public ToDoCommand(String description) {
        this.description = description;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        Task newTask = new ToDo(this.description);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask);
        return true;
    }
}
