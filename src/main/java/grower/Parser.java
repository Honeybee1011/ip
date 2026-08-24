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
        } else {
            return new AddCommand(userInput);
        }
        //return null;
    }
}