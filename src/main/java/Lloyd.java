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
        printResponse(BANNER + "\n Hello! I'm Lloyd.\n What can I do for you?");

        String[] toDoList = new String[100];
        int[] markedAsDone = new int[100];
        int taskCount = 0;
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
                    StringBuilder taskList = new StringBuilder();
                    for (int i = 0; i < taskCount; i++) {
                        String status = markedAsDone[i] == 1 ? "X" : " ";
                        taskList.append(String.format(
                                " %d. [%s] %s%n", i + 1, status, toDoList[i]
                        ));
                    }
                    printResponse(taskList.toString().stripTrailing());
                    break;
                case "mark":
                    if (commandParts.length < 2) {
                        printResponse(" Please provide the number of the task to mark.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(commandParts[1]);
                        if (taskNumber < 1 || taskNumber > taskCount) {
                            printResponse(" That task number does not exist.");
                            break;
                        }

                        markedAsDone[taskNumber - 1] = 1;
                        printResponse(" Nice! I've marked this task as done:\n   [X] "
                                + toDoList[taskNumber - 1]);
                    } catch (NumberFormatException e) {
                        printResponse(" Please provide a valid task number.");
                    }
                    break;
                case "unmark":
                    if (commandParts.length < 2) {
                        printResponse(" Please provide the number of the task to unmark.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(commandParts[1]);
                        if (taskNumber < 1 || taskNumber > taskCount) {
                            printResponse(" That task number does not exist.");
                            break;
                        }

                        markedAsDone[taskNumber - 1] = 0;
                        printResponse(" OK, I've marked this task as not done yet:\n   [ ] "
                                + toDoList[taskNumber - 1]);
                    } catch (NumberFormatException e) {
                        printResponse(" Please provide a valid task number.");
                    }
                    break;
                default:
                    if (taskCount == toDoList.length) {
                        printResponse(" Your task list is full.");
                        break;
                    }

                    toDoList[taskCount] = input;
                    markedAsDone[taskCount] = 0;
                    taskCount++;
                    printResponse(" Added: " + input);
                    break;
            }
        }
        scanner.close();

        printResponse(" Bye. Hope to see you again soon!");
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
