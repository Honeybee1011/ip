package grower;

import grower.commands.*;


public class Parser {
    public static Command parse(String userInput) {
        if (userInput.startsWith("echo ")) {
            String text = userInput.substring(5);
            return new EchoCommand(text);
        } else if (userInput.equalsIgnoreCase("bye")) {
            return new ByeCommand();
        } else if (userInput.equals("list")) {
            return new ListCommand();
        } else if (userInput.startsWith("mark ")) {
            String stringIndex = userInput.substring(5);
            int index = Integer.parseInt(stringIndex) - 1;
            return new MarkCommand(index);
        } else if (userInput.startsWith("unmark ")) {
            String stringIndex = userInput.substring(7);
            int index = Integer.parseInt(stringIndex) - 1;
            return new UnmarkCommand(index);
        } else if (userInput.startsWith("todo")) {
            String description = userInput.substring(5);
            return new ToDoCommand(description);
        } else if (userInput.startsWith("deadline")) {
            int startOfDeadline = userInput.indexOf("/");
            String description = userInput.substring(9, startOfDeadline);
            String deadline = userInput.substring(startOfDeadline + 4);
            return new DeadlineCommand(description, deadline);
        } else if (userInput.startsWith("event")) {
            int startOfStart = userInput.indexOf("/");
            int startofEnd = userInput.indexOf('/', userInput.indexOf('/') + 1);
            String description = userInput.substring(6, startOfStart);
            String start = userInput.substring(startOfStart + 6, startofEnd - 1);
            String end = userInput.substring(startofEnd + 4);
            return new EventCommand(description, start, end);
        }
        return null;
    }
}