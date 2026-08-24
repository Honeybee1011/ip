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
        } else {
            return new AddCommand(userInput);
        }
        //return null;
    }
}