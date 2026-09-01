package lloyd.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests how {@link Parser} separates command words from their arguments.
 */
public class ParserTest {

    @Test
    public void parse_recognizedCommandWithoutArguments_returnsCommandType() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("list");

        assertEquals(CommandType.LIST, parsedCommand.getCommandType());
        assertEquals("", parsedCommand.getArguments());
        assertFalse(parsedCommand.hasArguments());
    }

    @Test
    public void parse_commandWithArguments_returnsTypeAndArguments() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("todo read a book");

        assertEquals(CommandType.TODO, parsedCommand.getCommandType());
        assertEquals("read a book", parsedCommand.getArguments());
        assertTrue(parsedCommand.hasArguments());
    }

    @Test
    public void parse_findCommandWithKeyword_returnsFindTypeAndKeyword() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("find book");

        assertEquals(CommandType.FIND, parsedCommand.getCommandType());
        assertEquals("book", parsedCommand.getArguments());
    }

    @Test
    public void parse_extraWhitespace_trimsAndSeparatesInput() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("  deadline   submit report /by 02/09/2026  ");

        assertEquals(CommandType.DEADLINE, parsedCommand.getCommandType());
        assertEquals("submit report /by 02/09/2026", parsedCommand.getArguments());
    }

    @Test
    public void parse_unrecognizedCommand_returnsUnknownTypeWithArguments() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("hello Lloyd");

        assertEquals(CommandType.UNKNOWN, parsedCommand.getCommandType());
        assertEquals("Lloyd", parsedCommand.getArguments());
    }

    @Test
    public void parse_emptyInput_returnsUnknownTypeWithoutArguments() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("");

        assertEquals(CommandType.UNKNOWN, parsedCommand.getCommandType());
        assertEquals("", parsedCommand.getArguments());
        assertFalse(parsedCommand.hasArguments());
    }

    @Test
    public void parse_whitespaceOnlyInput_returnsUnknownTypeWithoutArguments() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("   ");

        assertEquals(CommandType.UNKNOWN, parsedCommand.getCommandType());
        assertEquals("", parsedCommand.getArguments());
        assertFalse(parsedCommand.hasArguments());
    }

    @Test
    public void parse_nullInput_throwsIllegalArgumentException() {
        Parser parser = new Parser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    }
}
