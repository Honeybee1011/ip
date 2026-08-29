/**
 * Represents the fixed set of commands understood by the Lloyd chatbot.
 */
public enum CommandType {
    BYE("bye"),
    LIST("list"),
    CHECK("check"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    UNKNOWN("");

    private final String commandWord;

    CommandType(String commandWord) {
        this.commandWord = commandWord;
    }

    /**
     * Finds the command type represented by a word entered by the user.
     *
     * @param commandWord first word of the user's input
     * @return matching command type, or {@link #UNKNOWN} when no command matches
     */
    public static CommandType from(String commandWord) {
        for (CommandType commandType : values()) {
            if (commandType.commandWord.equals(commandWord)) {
                return commandType;
            }
        }
        return UNKNOWN;
    }
}
