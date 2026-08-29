import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the chatbot's ordered collection of tasks.
 *
 * <p>This class keeps list operations out of the main application and prevents
 * callers from modifying the underlying collection directly.</p>
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks in their current order.
     *
     * @param tasks tasks with which to initialize the list
     */
    public TaskList(List<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks cannot be null");
        }
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Inserts a task at a specific zero-based position.
     *
     * <p>This operation allows the application to restore a deleted task if
     * saving the changed list fails.</p>
     *
     * @param index zero-based insertion position
     * @param task task to insert
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param index zero-based task position
     * @return task at the requested position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the most recently added task.
     *
     * @return last task in the list
     */
    public Task getLast() {
        return tasks.getLast();
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param index zero-based task position
     * @return removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Removes the last task in the list. */
    public void removeLast() {
        tasks.removeLast();
    }

    /**
     * Returns the current number of tasks.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Provides a read-only view for saving the current tasks.
     *
     * @return unmodifiable ordered task view
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
