package lloyd.command;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParserTest {
    
    @Test
    public void Parser_nullInput_throwsIllegalArgumentException() {
        Parser parser = new Parser();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    }

    @Test
    public void Parser_emptyInput_returnsParsedCommandWithUnknownType() {
        Parser parser = new Parser();
        ParsedCommand parsedCommand = parser.parse("");
        assert(parsedCommand.getCommandType() == CommandType.UNKNOWN);
        assert(parsedCommand.getArguments().isEmpty());
    }

    
}
