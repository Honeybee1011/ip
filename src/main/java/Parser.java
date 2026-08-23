import commands.Command;
import commands.EchoCommand;
import commands.ByeCommand;

public class Parser {
    public static Command parse(String userInput) {
        if (userInput.startsWith("echo ")) {
            String text = userInput.substring(5);
            return new EchoCommand(text);
        } else if (userInput.equalsIgnoreCase("bye")) {
            return new ByeCommand();
        }
        return null;
    }
}