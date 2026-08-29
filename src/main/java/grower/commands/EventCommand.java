package grower.commands;

import java.time.LocalDateTime;

import grower.tasks.TaskList;
import grower.ui.Ui;
import grower.tasks.Event;
import grower.tasks.Task;

public class EventCommand extends Command {
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public EventCommand(String description, LocalDateTime start, LocalDateTime end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        Task newTask = new Event(this.description, this.start, this.end);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask);
        return true;
    }
}
