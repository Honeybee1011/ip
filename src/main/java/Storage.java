import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves tasks to and loads tasks from a plain-text file.
 *
 * <p>Each field is separated by {@code " | "}. The first field identifies the
 * task type, and the second is {@code 1} for a completed task or {@code 0} for
 * an incomplete task.</p>
 */
public class Storage {
    private static final String DELIMITER = " | ";
    private static final String DELIMITER_REGEX = " \\| ";

    private final Path filePath;

    /**
     * Creates storage at the supplied path.
     *
     * @param filePath path of the text file used to store tasks
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks in their saved order.
     *
     * @return tasks read from the file
     * @throws IOException if the file cannot be read or contains invalid data
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            tasks.add(parseTask(line, i + 1));
        }
        return tasks;
    }

    /**
     * Replaces the storage file with the current ordered task list.
     *
     * @param tasks current tasks to save
     * @throws IOException if a directory or file cannot be written
     * @throws IllegalArgumentException if a task field contains a reserved character
     */
    public void save(List<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * Converts one task to its storage representation.
     */
    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = validateField(task.getDescription());

        if (task instanceof Todo) {
            return String.join(DELIMITER, "T", status, description);
        }
        if (task instanceof Deadline deadline) {
            return String.join(DELIMITER, "D", status, description,
                    validateField(deadline.getBy()));
        }
        if (task instanceof Event event) {
            return String.join(DELIMITER, "E", status, description,
                    validateField(event.getFrom()), validateField(event.getTo()));
        }

        throw new IllegalArgumentException(
                "Unsupported task type: " + task.getClass().getSimpleName());
    }

    /**
     * Recreates one task from a line in the storage file.
     */
    private Task parseTask(String line, int lineNumber) throws IOException {
        String[] fields = line.split(DELIMITER_REGEX, -1);

        try {
            Task task = switch (fields[0]) {
                case "T" -> {
                    requireFieldCount(fields, 3);
                    yield new Todo(requireNonEmpty(fields[2]));
                }
                case "D" -> {
                    requireFieldCount(fields, 4);
                    yield new Deadline(requireNonEmpty(fields[2]),
                            requireNonEmpty(fields[3]));
                }
                case "E" -> {
                    requireFieldCount(fields, 5);
                    yield new Event(requireNonEmpty(fields[2]),
                            requireNonEmpty(fields[3]), requireNonEmpty(fields[4]));
                }
                default -> throw new IllegalArgumentException("unknown task type");
            };

            if (fields[1].equals("1")) {
                task.mark();
            } else if (!fields[1].equals("0")) {
                throw new IllegalArgumentException("status must be 0 or 1");
            }
            return task;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Invalid task data on line " + lineNumber + ": " + line, e);
        }
    }

    /** Ensures that a saved line has the correct number of fields. */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("incorrect number of fields");
        }
    }

    /** Ensures that a required field contains information. */
    private String requireNonEmpty(String field) {
        if (field.isEmpty()) {
            throw new IllegalArgumentException("task fields cannot be empty");
        }
        return field;
    }

    /** Ensures that task data cannot break the line-oriented storage format. */
    private String validateField(String field) {
        if (field.contains("|") || field.contains("\n") || field.contains("\r")) {
            throw new IllegalArgumentException(
                    "Task fields cannot contain | or line-break characters");
        }
        return field;
    }
}
