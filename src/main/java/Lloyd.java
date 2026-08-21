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

        Task[] toDoList = new Task[100];
        int taskCount = 0;
        boolean isRunning = true;

        Scanner scanner = new Scanner(System.in);
        while (isRunning && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            String[] commandParts = input.split("\\s+", 2);
            String command = commandParts[0];

            boolean isAddCommand = command.equals("todo")
                    || command.equals("deadline")
                    || command.equals("event");

            if (isAddCommand && taskCount == toDoList.length) {
                printResponse(" Your task list is full.");
                continue;
            }

            switch (command) {
                case "bye":
                    isRunning = false;
                    break;
                case "list":
                    StringBuilder taskList = new StringBuilder();
                    for (int i = 0; i < taskCount; i++) {
                        taskList.append(String.format(
                                " %d. %s%n", i + 1, toDoList[i]
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

                        toDoList[taskNumber - 1].mark();
                        printResponse(" Nice! I've marked this task as done:\n"
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

                        toDoList[taskNumber - 1].unmark();
                        printResponse(" OK, I've marked this task as not done yet:\n"
                                + toDoList[taskNumber - 1]);
                    } catch (NumberFormatException e) {
                        printResponse(" Please provide a valid task number.");
                    }
                    break;
                case "todo":
                    toDoList[taskCount] = new Todo(input);
                    taskCount++;
                    String response = " Added: " + input + "\n Now you have " + taskCount + " tasks in the list.";
                    printResponse(response);
                    break;
                case "deadline":
                    if (commandParts.length < 2) {
                        printResponse(" Please provide a deadline description and /by date.");
                        break;
                    }

                    String deadlineDetails = commandParts[1];
                    int byIndex = deadlineDetails.indexOf(" /by ");

                    if (byIndex < 0) {
                        printResponse(" Please specify the deadline using /by.");
                        break;
                    }

                    String deadlineDescription =
                            deadlineDetails.substring(0, byIndex).trim();
                    String by =
                            deadlineDetails.substring(byIndex + " /by ".length()).trim();

                    if (deadlineDescription.isEmpty() || by.isEmpty()) {
                        printResponse(" Please provide both a description and a /by date.");
                        break;
                    }

                    toDoList[taskCount] = new Deadline(deadlineDescription, by);
                    taskCount++;

                    printResponse(" Got it. I've added this task:\n"
                            + toDoList[taskCount - 1]);
                    break;
                case "event":
                    if (commandParts.length < 2) {
                        printResponse(
                                " Please provide an event description, /from date, and /to date.");
                        break;
                    }

                    String eventDetails = commandParts[1];
                    int fromIndex = eventDetails.indexOf(" /from ");
                    int toIndex = eventDetails.indexOf(
                            " /to ", fromIndex + " /from ".length());

                    if (fromIndex < 0 || toIndex < 0) {
                        printResponse(" Please specify the event using /from and /to.");
                        break;
                    }

                    String eventDescription =
                            eventDetails.substring(0, fromIndex).trim();
                    String from = eventDetails.substring(
                            fromIndex + " /from ".length(), toIndex).trim();
                    String to =
                            eventDetails.substring(toIndex + " /to ".length()).trim();

                    if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        printResponse(
                                " Please provide a description, /from date, and /to date.");
                        break;
                    }

                    toDoList[taskCount] = new Event(eventDescription, from, to);
                    taskCount++;

                    printResponse(" Got it. I've added this task:\n"
                            + toDoList[taskCount - 1]);
                    break;
                default:
                    if (taskCount == toDoList.length) {
                        printResponse(" Your task list is full.");
                        break;
                    }

                    toDoList[taskCount] = new Task(input);
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
