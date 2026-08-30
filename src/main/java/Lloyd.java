import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Starts the Lloyd chatbot application and responds to commands entered by the user.
 */
public class Lloyd {
    private static final DateTimeFormatter DEADLINE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter EVENT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter CHECK_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private static final String BANNER =
            """
                     _      _                 _
                    | |    | |               | |
                    | |    | | ___  _   _  __| |
                    | |    | |/ _ \\| | | |/ _` |
                    | |____| | (_) | |_| | (_| |
                    |______|_|\\___/ \\__, |\\__,_|
                                     __/ |      \s
                                    |___/       \s""";

    /**
     * Runs the chatbot until the user enters the {@code bye} command.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        printResponse(BANNER
                + "\n Lloyd Frontera, the greatest estate developer, at your service!"
                + "\n Got a problem? Excellent. Problems are profits waiting for an engineer."
                + "\n Now, what needs doing?");

        Storage storage = new Storage(Path.of("data", "lloyd.txt"));
        TaskList toDoList;
        try {
            toDoList = new TaskList(storage.load());
        } catch (IOException e) {
            printResponse(" I could not load the task file. Check that data/lloyd.txt"
                    + " contains valid task data and can be read.");
            ui.close();
            return;
        }
        boolean isRunning = true;
        Parser parser = new Parser();

        while (isRunning && ui.hasNextCommand()) {
            ParsedCommand command = parser.parse(ui.readCommand());
            CommandType commandType = command.getCommandType();

            switch (commandType) {
                case BYE:
                    isRunning = false;
                    break;
                case LIST:
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
                case CHECK:
                    if (!command.hasArguments()) {
                        printResponse(" Tell me which date to inspect using dd/MM/yyyy.");
                        break;
                    }

                    try {
                        LocalDate checkedDate = LocalDate.parse(
                                command.getArguments(), DEADLINE_FORMAT);
                        StringBuilder scheduledTasks = new StringBuilder(
                                " Deadlines and event endpoints on "
                                        + checkedDate.format(CHECK_DISPLAY_FORMAT)
                                        + ":\n");
                        int matchCount = 0;
                        for (int i = 0; i < toDoList.size(); i++) {
                            Task task = toDoList.get(i);
                            if (isScheduledOn(task, checkedDate)) {
                                scheduledTasks.append(String.format(
                                        " %d.%s%n", i + 1, task));
                                matchCount++;
                            }
                        }

                        if (matchCount == 0) {
                            printResponse(" No deadlines or event endpoints fall on "
                                    + checkedDate.format(CHECK_DISPLAY_FORMAT) + ".");
                        } else {
                            printResponse(scheduledTasks.toString().stripTrailing());
                        }
                    } catch (DateTimeParseException e) {
                        printResponse(" Enter the date to check in dd/MM/yyyy format.");
                    }
                    break;
                case MARK:
                    if (!command.hasArguments()) {
                        printResponse(" Even I cannot finish an imaginary task."
                                + " Give me the task number to mark.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(command.getArguments());
                        if (taskNumber < 1 || taskNumber > toDoList.size()) {
                            printResponse(" That task is not in the master plan."
                                    + " Check its number.");
                            break;
                        }

                        Task task = toDoList.get(taskNumber - 1);
                        boolean wasDone = task.isDone();
                        task.mark();
                        if (!saveTasks(storage, toDoList)) {
                            if (!wasDone) {
                                task.unmark();
                            }
                            break;
                        }
                        printResponse(" Magnificent! Efficient work means lower costs."
                                + " This task is officially complete:\n"
                                + toDoList.get(taskNumber - 1));
                    } catch (NumberFormatException e) {
                        printResponse(" A task number needs to be a number."
                                + " Even Javier knows that.");
                    }
                    break;
                case UNMARK:
                    if (!command.hasArguments()) {
                        printResponse(" Rework requires paperwork."
                                + " Give me the task number to unmark.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(command.getArguments());
                        if (taskNumber < 1 || taskNumber > toDoList.size()) {
                            printResponse(" That task is not in the master plan."
                                    + " Check its number.");
                            break;
                        }

                        Task task = toDoList.get(taskNumber - 1);
                        boolean wasDone = task.isDone();
                        task.unmark();
                        if (!saveTasks(storage, toDoList)) {
                            if (wasDone) {
                                task.mark();
                            }
                            break;
                        }
                        printResponse(" What? Rework? That is terrible for the budget!"
                                + " Fine, this task is back under construction:\n"
                                + toDoList.get(taskNumber - 1));
                    } catch (NumberFormatException e) {
                        printResponse(" A task number needs to be a number."
                                + " Even Javier knows that.");
                    }
                    break;
                case DELETE:
                    if (!command.hasArguments()) {
                        printResponse(" Demolition needs a target."
                                + " Give me the task number to delete.");
                        break;
                    }

                    try {
                        int taskNumber = Integer.parseInt(command.getArguments());
                        if (taskNumber < 1 || taskNumber > toDoList.size()) {
                            printResponse(" That task is not in the master plan."
                                    + " Check its number.");
                            break;
                        }

                        Task deletedTask = toDoList.remove(taskNumber - 1);
                        if (!saveTasks(storage, toDoList)) {
                            toDoList.add(taskNumber - 1, deletedTask);
                            break;
                        }
                        printResponse(" Excellent! Waste eliminated from the budget."
                                + " I have removed this task:\n"
                                + deletedTask
                                + "\n Tasks currently in the master plan: "
                                + toDoList.size() + ".");
                    } catch (NumberFormatException e) {
                        printResponse(" A task number needs to be a number. Even Javier knows that.");
                    }
                    break;
                case TODO:
                    if (!command.hasArguments()) {
                        printResponse(" Every task needs a description."
                                + " Tell me what needs doing.");
                        break;
                    }

                    toDoList.add(new Todo(command.getArguments()));
                    if (!saveTasks(storage, toDoList)) {
                        toDoList.removeLast();
                        break;
                    }
                    printResponse(createTaskAddedMessage(
                            toDoList.getLast(), toDoList.size()
                    ));
                    break;
                case DEADLINE:
                    if (!command.hasArguments()) {
                        printResponse(" Every profitable project needs details. Provide a description and /by date.");
                        break;
                    }

                    String deadlineDetails = command.getArguments();
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

                    try {
                        LocalDate deadlineDate = LocalDate.parse(by, DEADLINE_FORMAT);
                        toDoList.add(new Deadline(deadlineDescription, deadlineDate));
                    } catch (DateTimeParseException e) {
                        printResponse(" Enter the deadline in dd/MM/yyyy format.");
                        break;
                    }
                    if (!saveTasks(storage, toDoList)) {
                        toDoList.removeLast();
                        break;
                    }

                    printResponse(createTaskAddedMessage(
                            toDoList.getLast(), toDoList.size()
                    ));
                    break;
                case EVENT:
                    if (!command.hasArguments()) {
                        printResponse(" Every grand event needs a plan. Provide a description, /from date, and /to date.");
                        break;
                    }

                    String eventDetails = command.getArguments();
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

                    try {
                        LocalDateTime start = LocalDateTime.parse(from, EVENT_FORMAT);
                        LocalDateTime end = LocalDateTime.parse(to, EVENT_FORMAT);
                        toDoList.add(new Event(eventDescription, start, end));
                    } catch (DateTimeParseException e) {
                        printResponse(" Enter event dates and times in dd/MM/yyyy HHmm format.");
                        break;
                    } catch (IllegalArgumentException e) {
                        printResponse(" The event end cannot be before its start.");
                        break;
                    }
                    if (!saveTasks(storage, toDoList)) {
                        toDoList.removeLast();
                        break;
                    }

                    printResponse(createTaskAddedMessage(
                            toDoList.getLast(), toDoList.size()
                    ));
                    break;
                default:
                    printResponse(" I reject vague contracts. Start every task with todo, deadline, or event.");
                    break;
            }
        }
        ui.close();

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
     * Reports whether a task belongs in the result for a checked date.
     * Deadlines match their due date. Events match only their start or end date,
     * so dates strictly between the endpoints of a multi-day event are excluded.
     *
     * @param task task to inspect
     * @param checkedDate date requested by the user
     * @return {@code true} if the deadline or an event endpoint matches the date
     */
    private static boolean isScheduledOn(Task task, LocalDate checkedDate) {
        if (task instanceof Deadline deadline) {
            return deadline.getBy().equals(checkedDate);
        }
        if (task instanceof Event event) {
            return event.getFrom().toLocalDate().equals(checkedDate)
                    || event.getTo().toLocalDate().equals(checkedDate);
        }
        return false;
    }

    /**
     * Saves the current tasks and reports a recoverable error to the user.
     *
     * @param storage storage used by the chatbot
     * @param tasks current task list
     * @return {@code true} when the save succeeds
     */
    private static boolean saveTasks(Storage storage, TaskList tasks) {
        try {
            storage.save(tasks.asList());
            return true;
        } catch (IOException | IllegalArgumentException e) {
            printResponse(" I could not save that change. The task list was left unchanged."
                    + " Check that data/lloyd.txt can be written and task details"
                    + " do not contain the | character.");
            return false;
        }
    }

    /**
     * Prints a chatbot response enclosed by divider lines.
     *
     * @param message response to display
     */
    private static void printResponse(String message) {
        Ui.showResponse(message);
    }
}
