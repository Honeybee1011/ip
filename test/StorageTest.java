import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a small console test driver for the {@link Storage} class.
 */
public class StorageTest {

    /**
     * Runs the storage checks and prints each successful result.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if the temporary test files cannot be accessed
     */
    public static void main(String[] args) throws IOException {
        Path testDirectory = Files.createTempDirectory("lloyd-storage-test-");

        testSaveFormat(testDirectory.resolve("nested").resolve("tasks.txt"));
        testLoad(testDirectory.resolve("load.txt"));
        testMissingFile(testDirectory.resolve("missing.txt"));
        testInvalidData(testDirectory.resolve("invalid.txt"));
        testReservedDelimiter(testDirectory.resolve("reserved.txt"));
    }

    /** Verifies the exact text format and automatic parent-directory creation. */
    private static void testSaveFormat(Path filePath) throws IOException {
        Todo todo = new Todo("read book");
        Deadline deadline = new Deadline("return book", "Sunday");
        deadline.mark();
        Event event = new Event("project meeting", "Monday 2pm", "Monday 4pm");

        new Storage(filePath).save(List.of(todo, deadline, event));

        List<String> expected = List.of(
                "T | 0 | read book",
                "D | 1 | return book | Sunday",
                "E | 0 | project meeting | Monday 2pm | Monday 4pm"
        );
        requireEquals(expected,
                Files.readAllLines(filePath, StandardCharsets.UTF_8),
                "saved file contents");
        System.out.println("Storage save format: PASSED");
    }

    /** Verifies task types, details, ordering, and completion state after loading. */
    private static void testLoad(Path filePath) throws IOException {
        Files.write(filePath, List.of(
                "T | 1 | read book",
                "D | 0 | return book | Sunday",
                "E | 1 | project meeting | Monday 2pm | Monday 4pm"
        ), StandardCharsets.UTF_8);

        ArrayList<Task> tasks = new Storage(filePath).load();

        requireEquals(3, tasks.size(), "loaded task count");
        requireEquals("[T][X] read book", tasks.get(0).toString(), "loaded todo");
        requireEquals("[D][ ] return book (by: Sunday)",
                tasks.get(1).toString(), "loaded deadline");
        requireEquals("[E][X] project meeting (from: Monday 2pm to: Monday 4pm)",
                tasks.get(2).toString(), "loaded event");
        System.out.println("Storage load: PASSED");
    }

    /** Verifies that first use does not require an existing file. */
    private static void testMissingFile(Path filePath) throws IOException {
        requireEquals(0, new Storage(filePath).load().size(),
                "task count for a missing file");
        System.out.println("Missing storage file: PASSED");
    }

    /** Verifies that malformed saved data is reported instead of silently ignored. */
    private static void testInvalidData(Path filePath) throws IOException {
        Files.writeString(filePath, "T | 2 | invalid status", StandardCharsets.UTF_8);

        try {
            new Storage(filePath).load();
            throw new AssertionError("Invalid saved data should cause an IOException");
        } catch (IOException expected) {
            requireEquals(true, expected.getMessage().contains("line 1"),
                    "invalid-data error line number");
        }
        System.out.println("Invalid storage data: PASSED");
    }

    /** Verifies that a field cannot contain the reserved delimiter character. */
    private static void testReservedDelimiter(Path filePath) throws IOException {
        try {
            new Storage(filePath).save(List.of(new Todo("compare A | B")));
            throw new AssertionError("A reserved delimiter should be rejected");
        } catch (IllegalArgumentException expected) {
            // Expected: accepting this character would make the saved line ambiguous.
        }
        System.out.println("Reserved delimiter validation: PASSED");
    }

    /** Fails the test immediately when expected and actual values differ. */
    private static void requireEquals(Object expected, Object actual, String checkName) {
        if (!expected.equals(actual)) {
            throw new AssertionError(checkName + ": expected " + expected + " but got " + actual);
        }
    }
}
