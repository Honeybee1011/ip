package grower;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import grower.commands.Command;
import grower.growerExceptions.GrowerException;
import grower.tasks.Task;

/**
 * The main class for the Grower application.
 * This class initializes the application and runs the main command loop.
 */

public class Grower {

    // This static TaskList serves as the single source of truth for task data.
    // It is declared here so other parts of the application (like commands) can access it.
    public static final ListTask taskList = new ListTask();

    public static void main(String[] args) {
        System.out.println("Goodday to you, I am Grow-er \n" +
                "your accountability partner! \n" +
                "i'm here to support your growth! \n" +
                "What can I do for you today \n");

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage("./data/grower.txt");

        //Attempt to load tasks from file
        try {
            List<String> savedTasks = storage.loadTasks();

            for (String taskData : savedTasks) {
                Task task = storage.parseTask(taskData);
                taskList.addLoadedTask(task);
            }
        } catch (IOException e) {
            System.out.println("Could not load saved tasks.");
        }

        //Boolean controlling if the chatbot should terminate or continue looping for user input cycle
        boolean continueRun = true;

        while (continueRun) {
            String userInput = scanner.nextLine();
            System.out.println("-------------------------------------------------------------");

            try {
                Command command = Parser.parse(userInput);
                continueRun = command.execute();
                storage.saveTasks(taskList.getTaskData());
            } catch (GrowerException e) {
                System.out.println(" " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not access the task data file, pls try again blud");
            }
            System.out.println("-------------------------------------------------------------");
        }
        scanner.close();
    }
}
