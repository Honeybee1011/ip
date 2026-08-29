package grower.commands;

import java.time.LocalDateTime;

import grower.tasks.TaskList;
import grower.ui.Ui;
import grower.tasks.Deadline;
import grower.tasks.Task;

public class DeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime deadline;

    public DeadlineCommand(String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        Task newTask = new Deadline(this.description, this.deadline);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask);
        return true;
    }
}
