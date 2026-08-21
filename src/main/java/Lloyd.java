import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Lloyd chatbot application and responds to commands entered by the user.
 */
public class Lloyd {
    private static final String DIVIDER =
            "____________________________________________________________";

    private static final String BANNER =
            " _      _                 _\n"
            + "| |    | |               | |\n"
            + "| |    | | ___  _   _  __| |\n"
            + "| |    | |/ _ \\| | | |/ _` |\n"
            + "| |____| | (_) | |_| | (_| |\n"
            + "|______|_|\\___/ \\__, |\\__,_|\n"
            + "                 __/ |       \n"
            + "                |___/        ";

    /**
     * Runs the chatbot until the user enters the {@code bye} command.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        printResponse(BANNER
                + "\n Lloyd Frontera, the greatest estate developer, at your service!"
                + "\n Got a problem? Excellent. Problems are profits waiting for an engineer."
                + "\n Now, what needs doing?");

        ArrayList<Task> toDoList = new ArrayList<>();
        boolean isRunning = true;

        Scanner scanner = new Scanner(System.in);
        while (isRunning && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            String[] commandParts = input.split("\\s+", 2);
            String command = commandParts[0];

            switch (command) {
                case "bye":
                    isRunning = false;
                    break;
                case "list":
                    StringBuilder taskList = new StringBuilder(
                            " Behold! Here is the master plan:\n"
                    );
                    for (int i = 0; i < toDoList.size(); i++) {
                        taskList.append(String.format(
                                " %d.%s%n", i + 1, toDoList.get(i)
                        ));
                    }
                    printResponse(taskList.toString().stripTrailing());
                    break;
                case "mark":
                    if (commandParts.length < 2) {
                        printResponse(" Even I cannot finish an imaginary task."
                                + " Give me the task number to mark.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(commandParts[1]);
                        if (taskNumber < 1 || taskNumber > toDoList.size()) {
                            printResponse(" That task is not in the master plan."
                                    + " Check its number.");
                            break;
                        }

                        toDoList.get(taskNumber - 1).mark();
                        printResponse(" Magnificent! Efficient work means lower costs."
                                + " This task is officially complete:\n"
                                + toDoList.get(taskNumber - 1));
                    } catch (NumberFormatException e) {
                        printResponse(" A task number needs to be a number."
                                + " Even Javier knows that.");
                    }
                    break;
                case "unmark":
                    if (commandParts.length < 2) {
                        printResponse(" Rework requires paperwork."
                                + " Give me the task number to unmark.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(commandParts[1]);
                        if (taskNumber < 1 || taskNumber > toDoList.size()) {
                            printResponse(" That task is not in the master plan."
                                    + " Check its number.");
                            break;
                        }

                        toDoList.get(taskNumber - 1).unmark();
                        printResponse(" What? Rework? That is terrible for the budget!"
                                + " Fine, this task is back under construction:\n"
                                + toDoList.get(taskNumber - 1));
                    } catch (NumberFormatException e) {
                        printResponse(" A task number needs to be a number."
                                + " Even Javier knows that.");
                    }
                    break;
                case "delete":
                    if (commandParts.length < 2) {
                        printResponse(" Demolition needs a target."
                                + " Give me the task number to delete.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(commandParts[1]);
                        if (taskNumber < 1 || taskNumber > toDoList.size()) {
                            printResponse(" That task is not in the master plan."
                                    + " Check its number.");
                            break;
                        }

                        Task deletedTask = toDoList.remove(taskNumber - 1);
                        printResponse(" Excellent! Waste eliminated from the budget."
                                + " I have removed this task:\n"
                                + deletedTask
                                + "\n Tasks currently in the master plan: "
                                + toDoList.size() + ".");
                    } catch (NumberFormatException e) {
                        printResponse(" A task number needs to be a number. Even Javier knows that.");
                    }
                    break;
                case "todo":
                    toDoList.add(new Todo(commandParts[1]));
                    printResponse(createTaskAddedMessage(
                            toDoList.get(toDoList.size() - 1), toDoList.size()
                    ));
                    break;
                case "deadline":
                    if (commandParts.length < 2) {
                        printResponse(" Every profitable project needs details. Provide a description and /by date.");
                        break;
                    }

                    String deadlineDetails = commandParts[1];
                    int byIndex = deadlineDetails.indexOf(" /by ");

                    if (byIndex < 0) {
                        printResponse(" No deadline, no schedule. Specify it using /by.");
                        break;
                    }

                    String deadlineDescription =
                            deadlineDetails.substring(0, byIndex).trim();
                    String by =
                            deadlineDetails.substring(byIndex + " /by ".length()).trim();

                    if (deadlineDescription.isEmpty() || by.isEmpty()) {
                        printResponse(" A contract needs both the work and its deadline. Provide a description and /by date.");
                        break;
                    }

                    toDoList.add(new Deadline(deadlineDescription, by));

                    printResponse(createTaskAddedMessage(
                            toDoList.get(toDoList.size() - 1), toDoList.size()
                    ));
                    break;
                case "event":
                    if (commandParts.length < 2) {
                        printResponse(" Every grand event needs a plan. Provide a description, /from date, and /to date.");
                        break;
                    }

                    String eventDetails = commandParts[1];
                    int fromIndex = eventDetails.indexOf(" /from ");
                    int toIndex = eventDetails.indexOf(
                            " /to ", fromIndex + " /from ".length());

                    if (fromIndex < 0 || toIndex < 0) {
                        printResponse(" An event without a schedule invites disaster. Specify it using /from and /to.");
                        break;
                    }

                    String eventDescription =
                            eventDetails.substring(0, fromIndex).trim();
                    String from = eventDetails.substring(
                            fromIndex + " /from ".length(), toIndex).trim();
                    String to =
                            eventDetails.substring(toIndex + " /to ".length()).trim();

                    if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        printResponse(" The project contract is incomplete. Provide a description, /from date, and /to date.");
                        break;
                    }

                    toDoList.add(new Event(eventDescription, from, to));

                    printResponse(createTaskAddedMessage(
                            toDoList.get(toDoList.size() - 1), toDoList.size()
                    ));
                    break;
                default:
                    printResponse(" I reject vague contracts. Start every task with todo, deadline, or event.");
                    break;
            }
        }
        scanner.close();

        printResponse(" Leaving already? Fine. Rest while you can; those tasks will not"
                + " build themselves. Come back when you are ready to work..."
                + " and remember to bring payment!");
    }

    /**
     * Creates the standard response shown after adding any type of task.
     *
     * @param task task that was added
     * @param taskCount number of tasks currently in the list
     * @return response containing the added task and updated task count
     */
    private static String createTaskAddedMessage(Task task, int taskCount) {
        return " Excellent! Another investment in your future has been approved:\n"
                + "   " + task
                + "\n Tasks currently in the master plan: " + taskCount + ".";
    }

    /**
     * Prints a chatbot response enclosed by divider lines.
     *
     * @param message response to display
     */
    private static void printResponse(String message) {
        System.out.println(DIVIDER);
        System.out.println(message);
        System.out.println(DIVIDER);
        System.out.println();
    }
}
