package grower;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
}

