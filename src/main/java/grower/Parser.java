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
        } else if (userInput.startsWith("deadline")){
            int startOfDeadline = userInput.indexOf("/");
            String description = userInput.substring(9, startOfDeadline);
            String deadline = userInput.substring(startOfDeadline + 1);
            return new DeadlineCommand(description, deadline);
        }
        return null;
    }
}