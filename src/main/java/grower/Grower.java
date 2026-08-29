package grower;

import java.io.IOException;
import java.util.List;

import grower.commands.Command;
import grower.exceptions.GrowerException;
import grower.parser.Parser;
import grower.storage.Storage;
import grower.tasks.Task;
import grower.tasks.TaskList;
import grower.ui.Ui;

/**
 * Runs the Grower task-management application.
 */
public class Grower {
    /**
     * Starts the application and runs its command loop.
     *
     * @param args Command-line arguments; currently unused.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList taskList = new TaskList();
        Storage storage = new Storage("./data/grower.txt");

        ui.showWelcome();

        //Attempt to load tasks from file
        try {
            List<String> savedTasks = storage.loadTasks();

            for (String taskData : savedTasks) {
                try {
                    Task task = storage.parseTask(taskData);
                    taskList.addTask(task);
                } catch (GrowerException e) {
                    ui.showError(e.getMessage());
                }
            }
        } catch (IOException e) {
            ui.showError("Could not load saved tasks.");
        }

        //Boolean controlling if the chatbot should terminate or continue looping for user input cycle
        boolean continueRun = true;

        //Loops through getting user input, executing command, checking if run should continue
        while (continueRun) {
            try {
                String input = ui.readCommand();
                ui.showSeparator();
                Command command = Parser.parse(input);
                continueRun = command.execute(taskList, ui);
                storage.saveTasks(taskList.getTaskData());
            } catch (GrowerException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("Could not access the task data file, pls try again blud");
            }
            ui.showSeparator();
        }
        ui.close();
    }
}
