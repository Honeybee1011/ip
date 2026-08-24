package grower;

import java.util.Scanner;
import grower.commands.Command;
import grower.growerExceptions.GrowerException;

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
        boolean continueRun = true;

        while (continueRun) {
            String userInput = scanner.nextLine();
            System.out.println("_________________________________");

            try {
                Command command = Parser.parse(userInput);
                continueRun = command.execute();
            } catch (GrowerException e) {
                System.out.println(" " + e.getMessage());
            }
            System.out.println("_________________________________");
        }
        scanner.close();
    }
}
