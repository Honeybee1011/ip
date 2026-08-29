package grower.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import grower.growerExceptions.GrowerException;
import grower.tasks.Task;
import grower.tasks.ToDo;
import grower.tasks.Deadline;
import grower.tasks.Event;

/**
 * Handles saving tasks to file and loading tasks from file
 */

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Saves the serialized tasks, replacing the previous file contents.
     */
    public void saveTasks(List<String> taskData) throws IOException {
        Path parentDirectory = filePath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        Files.write(filePath, taskData);
    }

    /**
     * Reads all serialized tasks from the data file.
     */
    public List<String> loadTasks() throws IOException {
        if (!Files.exists(filePath)) {
            return List.of();
        }

        return Files.readAllLines(filePath);
    }

    /**
     * Parses input file from string to tasks
     */
    public Task parseTask(String line) throws GrowerException {
        String[] parts = line.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new GrowerException("Saved task has missing fields: " + line);
        }

        String type = parts[0];
        boolean completed = parts[1].equals("1");
        String description = parts[2];

        Task task;

        try {
            switch (type) {
                case "T":
                    task = new ToDo(description);
                    break;
                case "D":
                    ensureFieldCount(parts, 4, line);
                    task = new Deadline(
                            description,
                            LocalDateTime.parse(parts[3], DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    break;
                case "E":
                    ensureFieldCount(parts, 5, line);
                    LocalDateTime start = LocalDateTime.parse(
                            parts[3], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    LocalDateTime end = LocalDateTime.parse(
                            parts[4], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    if (!end.isAfter(start)) {
                        throw new GrowerException("Saved event ends before it starts: " + line);
                    }
                    task = new Event(description, start, end);
                    break;
                default:
                    throw new GrowerException("Unknown saved task type: " + type);
            }
        } catch (DateTimeParseException e) {
            throw new GrowerException("Saved task has an invalid date and was skipped: " + line);
        }

        if (completed) {
            task.mark();
        }

        return task;
    }

    /**
     * Checks that a serialized task contains every field required by its type.
     */
    private void ensureFieldCount(String[] parts, int requiredCount, String line) throws GrowerException {
        if (parts.length < requiredCount) {
            throw new GrowerException("Saved task has missing fields: " + line);
        }
    }
}
