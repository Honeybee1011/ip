package grower;

import grower.commands.*;
import grower.growerExceptions.*;


public class Parser {
    /**
     * Parses user input into a command for execution.
     *
     * @param userInput The full user input string.
     * @return The command object ready for execution.
     * @throws GrowerException If the user input is invalid or malformed.
     */
    public static Command parse(String userInput) throws GrowerException {
        // Split the input into the command word and the arguments.
        // The "2" limits the split to at most two parts.
        String[] parts = userInput.trim().split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
        case "bye":
            return new ByeCommand();
        case "list":
            return new ListCommand();
        case "mark":

        case "unmark":
            if (args.isEmpty()) {
                throw new GrowerException("You must provide a task number to " + commandWord + ".");
            }
            try {
                int index = Integer.parseInt(args) - 1;
                return commandWord.equals("mark") ? new MarkCommand(index) : new UnmarkCommand(index);
            } catch (NumberFormatException e) {
                throw new GrowerException("The task number must be an integer.");
            }
        case "todo":
            if (args.isEmpty()) {
                throw new MissingDescriptionException("The description for a todo cannot be empty.");
            }
            return new ToDoCommand(args);
        case "deadline":
            if (args.isEmpty()) {
                throw new MissingDescriptionException("The description for a deadline cannot be empty.");
            }
            String[] deadlineParts = args.split(" /by ", 2);
            if (deadlineParts.length < 2) {
                throw new GrowerException("Invalid deadline format. Use: deadline <description> /by <date>");
            }
            return new DeadlineCommand(deadlineParts[0], deadlineParts[1]);
        case "event":
            if (args.isEmpty()) {
                throw new MissingDescriptionException("The description for an event cannot be empty.");
            }
            String[] eventParts = args.split(" /from ", 2);
            if (eventParts.length < 2) {
                throw new GrowerException("Invalid event format. Use: event <desc> /from <start> /to <end>");
            }
            String[] timeParts = eventParts[1].split(" /to ", 2);
            if (timeParts.length < 2) {
                throw new GrowerException("Invalid event format. Use: event <desc> /from <start> /to <end>");
            }
            return new EventCommand(eventParts[0], timeParts[0], timeParts[1]);
        case "echo":
            if (args.isEmpty()) {
                throw new MissingDescriptionException("There is nothing to echo!");
            }
            return new EchoCommand(args);
        default:
            // If the command is not recognized, throw an exception.
            throw new UnknownCommandException("I'm sorry, but I don't know what that means :-(");
        }
    }
}