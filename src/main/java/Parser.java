/**
 * Interprets raw user input as a command type and its argument text.
 */
public class Parser {

    /**
     * Parses one line entered by the user.
     *
     * @param input raw user input
     * @return recognized command and its arguments
     */
    public ParsedCommand parse(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        String[] commandParts = input.trim().split("\\s+", 2);
        CommandType commandType = CommandType.from(commandParts[0]);
        String arguments = commandParts.length < 2 ? "" : commandParts[1];
        return new ParsedCommand(commandType, arguments);
    }
}
