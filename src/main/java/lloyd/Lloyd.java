package lloyd;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import lloyd.command.ParsedCommand;
import lloyd.command.Parser;
import lloyd.storage.Storage;
import lloyd.task.Deadline;
import lloyd.task.Event;
import lloyd.task.Task;
import lloyd.task.TaskList;
import lloyd.task.Todo;
import lloyd.ui.Ui;

/** Processes commands and manages the persistent task list for Lloyd's user interfaces. */
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
    private static final String GREETING =
            " Lloyd Frontera, the greatest estate developer, at your service!"
                    + "\n Got a problem? Excellent. Problems are profits waiting for an engineer."
                    + "\n Now, what needs doing?";
    private static final String FAREWELL =
            " Leaving already? Fine. Rest while you can; those tasks will not"
                    + " build themselves. Come back when you are ready to work..."
                    + " and remember to bring payment!";
    private static final String LOAD_ERROR =
            " I could not load the task file. Check that data/lloyd.txt"
                    + " contains valid task data and can be read.";
    private static final String SAVE_ERROR =
            " I could not save that change. The task list was left unchanged."
                    + " Check that data/lloyd.txt can be written and task details"
                    + " do not contain the | character.";

    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;
    private boolean isExitRequested;

    /**
     * Creates a Lloyd application backed by the default task file.
     *
     * @throws IOException If the task file cannot be loaded.
     */
    public Lloyd() throws IOException {
        this(Path.of("data", "lloyd.txt"));
    }

    /**
     * Creates a Lloyd application backed by the supplied task file.
     *
     * @param storagePath Location from which tasks are loaded and saved.
     * @throws IOException If the task file cannot be loaded.
     */
    public Lloyd(Path storagePath) throws IOException {
        storage = new Storage(storagePath);
        taskList = new TaskList(storage.load());
        parser = new Parser();
    }

    /**
     * Runs Lloyd through the original console interface.
     *
     * <p>The graphical application uses {@link #getResponse(String)} directly;
     * this entry point remains available for console testing.</p>
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        printResponse(BANNER + "\n" + GREETING);

        try {
            Lloyd lloyd = new Lloyd();
            while (!lloyd.isExitRequested() && ui.hasNextCommand()) {
                printResponse(lloyd.getResponse(ui.readCommand()));
            }
            if (!lloyd.isExitRequested()) {
                printResponse(FAREWELL);
            }
        } catch (IOException e) {
            printResponse(LOAD_ERROR);
        } finally {
            ui.close();
        }
    }

    /**
     * Returns the greeting shown when a user interface starts.
     *
     * @return Lloyd's greeting without the console-only text banner.
     */
    public String getGreeting() {
        return GREETING.stripLeading();
    }

    /**
     * Processes one command and returns the response for a user interface to display.
     *
     * @param input Command entered by the user.
     * @return Lloyd's response to the command.
     */
    public String getResponse(String input) {
        ParsedCommand command = parser.parse(input);
        return switch (command.getCommandType()) {
            case BYE -> exit();
            case LIST -> listTasks();
            case FIND -> findTasks(command);
            case CHECK -> checkDate(command);
            case MARK -> markTask(command);
            case UNMARK -> unmarkTask(command);
            case DELETE -> deleteTask(command);
            case TODO -> addTodo(command);
            case DEADLINE -> addDeadline(command);
            case EVENT -> addEvent(command);
            default -> " I reject vague contracts."
                    + " Start every task with todo, deadline, or event.";
        };
    }

    /**
     * Reports whether the latest command asked Lloyd to exit.
     *
     * @return {@code true} after Lloyd processes the {@code bye} command.
     */
    public boolean isExitRequested() {
        return isExitRequested;
    }

    /** Marks the application as finished and returns Lloyd's farewell. */
    private String exit() {
        isExitRequested = true;
        return FAREWELL;
    }

    /** Returns all tasks in their current order. */
    private String listTasks() {
        StringBuilder numberedTasks = new StringBuilder(
                " Behold! Here is the master plan:\n");
        for (int i = 0; i < taskList.size(); i++) {
            numberedTasks.append(String.format(" %d.%s%n", i + 1, taskList.get(i)));
        }
        return numberedTasks.toString().stripTrailing();
    }

    /** Returns tasks containing the requested keyword. */
    private String findTasks(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " A search needs a keyword. Tell me what to find.";
        }

        String keyword = command.getArguments();
        TaskList matchingTasks = new TaskList(taskList.find(keyword));
        if (matchingTasks.size() == 0) {
            return " No tasks contain the keyword: " + keyword;
        }

        StringBuilder searchResult = new StringBuilder(
                " Here are the matching tasks in the master plan:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            searchResult.append(String.format(
                    " %d.%s%n", i + 1, matchingTasks.get(i)));
        }
        return searchResult.toString().stripTrailing();
    }

    /** Returns deadlines and event endpoints on the requested date. */
    private String checkDate(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Tell me which date to inspect using dd/MM/yyyy.";
        }

        try {
            LocalDate checkedDate = LocalDate.parse(
                    command.getArguments(), DEADLINE_FORMAT);
            StringBuilder scheduledTasks = new StringBuilder(
                    " Deadlines and event endpoints on "
                            + checkedDate.format(CHECK_DISPLAY_FORMAT) + ":\n");
            int matchCount = 0;
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                if (isScheduledOn(task, checkedDate)) {
                    scheduledTasks.append(String.format(
                            " %d.%s%n", i + 1, task));
                    matchCount++;
                }
            }

            if (matchCount == 0) {
                return " No deadlines or event endpoints fall on "
                        + checkedDate.format(CHECK_DISPLAY_FORMAT) + ".";
            }
            return scheduledTasks.toString().stripTrailing();
        } catch (DateTimeParseException e) {
            return " Enter the date to check in dd/MM/yyyy format.";
        }
    }

    /** Marks one task as complete when its number is valid. */
    private String markTask(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Even I cannot finish an imaginary task."
                    + " Give me the task number to mark.";
        }

        try {
            int taskNumber = Integer.parseInt(command.getArguments());
            if (!isValidTaskNumber(taskNumber)) {
                return invalidTaskNumberMessage();
            }

            Task task = taskList.get(taskNumber - 1);
            boolean wasDone = task.isDone();
            task.mark();
            if (!saveTasks()) {
                if (!wasDone) {
                    task.unmark();
                }
                return SAVE_ERROR;
            }
            return " Magnificent! Efficient work means lower costs."
                    + " This task is officially complete:\n" + task;
        } catch (NumberFormatException e) {
            return invalidNumberMessage();
        }
    }

    /** Marks one task as incomplete when its number is valid. */
    private String unmarkTask(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Rework requires paperwork."
                    + " Give me the task number to unmark.";
        }

        try {
            int taskNumber = Integer.parseInt(command.getArguments());
            if (!isValidTaskNumber(taskNumber)) {
                return invalidTaskNumberMessage();
            }

            Task task = taskList.get(taskNumber - 1);
            boolean wasDone = task.isDone();
            task.unmark();
            if (!saveTasks()) {
                if (wasDone) {
                    task.mark();
                }
                return SAVE_ERROR;
            }
            return " What? Rework? That is terrible for the budget!"
                    + " Fine, this task is back under construction:\n" + task;
        } catch (NumberFormatException e) {
            return invalidNumberMessage();
        }
    }

    /** Deletes one task when its number is valid. */
    private String deleteTask(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Demolition needs a target."
                    + " Give me the task number to delete.";
        }

        try {
            int taskNumber = Integer.parseInt(command.getArguments());
            if (!isValidTaskNumber(taskNumber)) {
                return invalidTaskNumberMessage();
            }

            Task deletedTask = taskList.remove(taskNumber - 1);
            if (!saveTasks()) {
                taskList.add(taskNumber - 1, deletedTask);
                return SAVE_ERROR;
            }
            return " Excellent! Waste eliminated from the budget."
                    + " I have removed this task:\n" + deletedTask
                    + "\n Tasks currently in the master plan: "
                    + taskList.size() + ".";
        } catch (NumberFormatException e) {
            return invalidNumberMessage();
        }
    }

    /** Adds a todo when a description is present. */
    private String addTodo(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Every task needs a description. Tell me what needs doing.";
        }

        taskList.add(new Todo(command.getArguments()));
        if (!saveTasks()) {
            taskList.removeLast();
            return SAVE_ERROR;
        }
        return createTaskAddedMessage(taskList.getLast(), taskList.size());
    }

    /** Adds a deadline when its description and date are valid. */
    private String addDeadline(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Every profitable project needs details."
                    + " Provide a description and /by date.";
        }

        String deadlineDetails = command.getArguments();
        int byIndex = deadlineDetails.indexOf(" /by ");
        if (byIndex < 0) {
            return " No deadline, no schedule. Specify it using /by.";
        }

        String deadlineDescription = deadlineDetails.substring(0, byIndex).trim();
        String by = deadlineDetails.substring(byIndex + " /by ".length()).trim();
        if (deadlineDescription.isEmpty() || by.isEmpty()) {
            return " A contract needs both the work and its deadline."
                    + " Provide a description and /by date.";
        }

        try {
            LocalDate deadlineDate = LocalDate.parse(by, DEADLINE_FORMAT);
            taskList.add(new Deadline(deadlineDescription, deadlineDate));
        } catch (DateTimeParseException e) {
            return " Enter the deadline in dd/MM/yyyy format.";
        }

        if (!saveTasks()) {
            taskList.removeLast();
            return SAVE_ERROR;
        }
        return createTaskAddedMessage(taskList.getLast(), taskList.size());
    }

    /** Adds an event when its description and endpoints are valid. */
    private String addEvent(ParsedCommand command) {
        if (!command.hasArguments()) {
            return " Every grand event needs a plan."
                    + " Provide a description, /from date, and /to date.";
        }

        String eventDetails = command.getArguments();
        int fromIndex = eventDetails.indexOf(" /from ");
        int toIndex = eventDetails.indexOf(
                " /to ", fromIndex + " /from ".length());
        if (fromIndex < 0 || toIndex < 0) {
            return " An event without a schedule invites disaster."
                    + " Specify it using /from and /to.";
        }

        String eventDescription = eventDetails.substring(0, fromIndex).trim();
        String from = eventDetails.substring(
                fromIndex + " /from ".length(), toIndex).trim();
        String to = eventDetails.substring(toIndex + " /to ".length()).trim();
        if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
            return " The project contract is incomplete."
                    + " Provide a description, /from date, and /to date.";
        }

        try {
            LocalDateTime start = LocalDateTime.parse(from, EVENT_FORMAT);
            LocalDateTime end = LocalDateTime.parse(to, EVENT_FORMAT);
            taskList.add(new Event(eventDescription, start, end));
        } catch (DateTimeParseException e) {
            return " Enter event dates and times in dd/MM/yyyy HHmm format.";
        } catch (IllegalArgumentException e) {
            return " The event end cannot be before its start.";
        }

        if (!saveTasks()) {
            taskList.removeLast();
            return SAVE_ERROR;
        }
        return createTaskAddedMessage(taskList.getLast(), taskList.size());
    }

    /** Reports whether a one-based task number refers to an existing task. */
    private boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= taskList.size();
    }

    /** Returns the standard response for a numeric task index outside the list. */
    private String invalidTaskNumberMessage() {
        return " That task is not in the master plan. Check its number.";
    }

    /** Returns the standard response for a task number that is not numeric. */
    private String invalidNumberMessage() {
        return " A task number needs to be a number. Even Javier knows that.";
    }

    /** Creates the standard response shown after adding any type of task. */
    private String createTaskAddedMessage(Task task, int taskCount) {
        return " Excellent! Another investment in your future has been approved:\n"
                + "   " + task
                + "\n Tasks currently in the master plan: " + taskCount + ".";
    }

    /** Reports whether a deadline or event endpoint matches the checked date. */
    private boolean isScheduledOn(Task task, LocalDate checkedDate) {
        if (task instanceof Deadline deadline) {
            return deadline.getBy().equals(checkedDate);
        }
        if (task instanceof Event event) {
            return event.getFrom().toLocalDate().equals(checkedDate)
                    || event.getTo().toLocalDate().equals(checkedDate);
        }
        return false;
    }

    /** Saves the current tasks and reports whether the operation succeeded. */
    private boolean saveTasks() {
        try {
            storage.save(taskList.asList());
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

    /** Displays a response through the compatibility console interface. */
    private static void printResponse(String message) {
        Ui.showResponse(message);
    }
}
