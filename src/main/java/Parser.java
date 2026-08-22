import commands.Command;
import commands.EchoCommand;

public class Parser {
    public static Command parse(String userInput) {
        if (userInput.startsWith("echo ")) {
            String text = userInput.substring(5);
            return new EchoCommand(text);
        }        return null;
    }
}