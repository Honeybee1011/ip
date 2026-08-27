package grower;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    public Task parseTask(String line) {
        String[] parts = line.split(" \\| ", -1);

        String type = parts[0];
        boolean completed = parts[1].equals("1");
        String description = parts[2];

        Task task;

        switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (completed) {
            task.mark();
        }

        return task;
    }
}

