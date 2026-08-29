package grower.commands;

import java.util.List;
import grower.tasks.TaskList;
import grower.tasks.Task;
import grower.ui.Ui;
/**
 * A class created by the find command and returns a filtered list
 */

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui) {
        List<Task> matches = tasks.findTasks(keyword);
        ui.showSearchResults(matches);
        return true;
    }
}