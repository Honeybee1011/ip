package grower.tasks;

import java.util.ArrayList;
import java.util.List;

import grower.growerExceptions.InvalidTaskNumberException;

public class TaskList {
    private final List<Task> listOfTasks;

    public TaskList() {
        listOfTasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        listOfTasks.add(task);
    }

    public Task markTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        Task task = listOfTasks.get(index);
        task.mark();
        return task;
    }

    public Task unmarkTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        Task task = listOfTasks.get(index);
        task.unmark();
        return task;
    }

    public Task deleteTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        return listOfTasks.remove(index);
    }

    /**
     * Returns a read-only snapshot of the tasks currently in the list.
     *
     * @return tasks in their current order
     */
    public List<Task> getTasks() {
        return List.copyOf(listOfTasks);
    }

    /**
     * Checks that an index refers to an existing task.
     *
     * @param index the zero-based index to check
     * @throws InvalidTaskNumberException if the index is outside the task list
     */
    private void validateIndex(int index) throws InvalidTaskNumberException {
        if (index < 0 || index >= listOfTasks.size()) {
            throw new InvalidTaskNumberException(index, listOfTasks.size());
        }
    }

    /**
     * Gets all task data and writes each task as a string in a List so as to write to file
     */
    public List<String> getTaskData() {
        return listOfTasks.stream()
                .map(Task::toFileString)
                .toList();
    }
}
