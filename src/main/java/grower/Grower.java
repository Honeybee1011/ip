package grower;

import java.io.IOException;
import java.util.List;

import grower.commands.Command;
import grower.growerExceptions.GrowerException;
import grower.tasks.Task;

/**
 * The main class for the Grower application.
 * This class initializes the application and runs the main command loop.
 */

public class Grower {
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
