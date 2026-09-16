package lloyd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests help output, validation, and preservation of application state. */
public class LloydHelpTest {
    private static final String EXPECTED_HELP =
            """
                    Here's what I can do:

                    Add tasks:
                      todo DESCRIPTION
                      deadline DESCRIPTION /by DD/MM/YYYY
                      event DESCRIPTION /from DD/MM/YYYY HHMM /to DD/MM/YYYY HHMM

                    View tasks:
                      list
                      find KEYWORD
                      check DD/MM/YYYY
                      reminder

                    Manage tasks:
                      mark TASK_NUMBER
                      unmark TASK_NUMBER
                      delete TASK_NUMBER

                    Other commands:
                      help
                      bye

                    Dates use DD/MM/YYYY.
                    Times use the 24-hour HHMM format; for example, 1430 means 2:30 PM.""";

    @TempDir
    private Path temporaryDirectory;

    /** Verifies that help returns the complete command summary with a confident mood. */
    @Test
    public void help_withoutArguments_returnsCommandSummary() throws IOException {
        Lloyd lloyd = createLloyd("");

        LloydResponse response = lloyd.getReply("help");

        assertEquals(EXPECTED_HELP, response.text());
        assertEquals(LloydMood.CONFIDENT, response.mood());
        assertFalse(lloyd.isExitRequested());
    }

    /** Verifies that help can be read without changing existing task data. */
    @Test
    public void help_withExistingTasks_preservesStorage() throws IOException {
        String storageContents = "T | 0 | inspect foundations";
        Path storagePath = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(storagePath, storageContents, StandardCharsets.UTF_8);
        Lloyd lloyd = new Lloyd(storagePath);

        lloyd.getResponse("help");

        assertEquals(storageContents, Files.readString(storagePath, StandardCharsets.UTF_8));
        assertEquals("Tasks:\n1. [T][ ] inspect foundations", lloyd.getResponse("list"));
    }

    /** Verifies that help arguments are rejected with an annoyed mood. */
    @Test
    public void help_withArguments_returnsValidationMessage() throws IOException {
        Lloyd lloyd = createLloyd("");

        LloydResponse response = lloyd.getReply("help tasks");

        assertEquals("Enter help without additional information.", response.text());
        assertEquals(LloydMood.ANNOYED, response.mood());
    }

    /** Creates a Lloyd instance backed by the supplied storage contents. */
    private Lloyd createLloyd(String storageContents) throws IOException {
        Path storagePath = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(storagePath, storageContents, StandardCharsets.UTF_8);
        return new Lloyd(storagePath);
    }
}
