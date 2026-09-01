package lloyd.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests conversion of command words into {@link CommandType} values.
 */
public class CommandTypeTest {

    @Test
    public void from_recognizedCommandWord_returnsMatchingCommandType() {
        assertEquals(CommandType.BYE, CommandType.from("bye"));
        assertEquals(CommandType.LIST, CommandType.from("list"));
        assertEquals(CommandType.CHECK, CommandType.from("check"));
        assertEquals(CommandType.MARK, CommandType.from("mark"));
        assertEquals(CommandType.UNMARK, CommandType.from("unmark"));
        assertEquals(CommandType.DELETE, CommandType.from("delete"));
        assertEquals(CommandType.TODO, CommandType.from("todo"));
        assertEquals(CommandType.DEADLINE, CommandType.from("deadline"));
        assertEquals(CommandType.EVENT, CommandType.from("event"));
    }

    @Test
    public void from_unrecognizedCommandWord_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("hello"));
    }

    @Test
    public void from_emptyCommandWord_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from(""));
    }

    @Test
    public void from_nullCommandWord_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from(null));
    }

    @Test
    public void from_differentlyCasedCommandWord_returnsUnknown() {
        assertEquals(CommandType.UNKNOWN, CommandType.from("TODO"));
    }
}
