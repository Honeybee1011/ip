package grower;

import java.util.ArrayList;
import java.util.List;

import grower.growerExceptions.InvalidTaskNumberException;
import grower.tasks.Task;

public class ListTask {
    private final ArrayList<Task> listOfTasks;

    public ListTask() {
        listOfTasks = new ArrayList<>();
    }

    public boolean addTask(Task newTask) {
        this.listOfTasks.add(newTask);
        System.out.println(" added: " + newTask);
        return true;
    }

    public void addLoadedTask(Task task) {
        listOfTasks.add(task);
    }

    public void markTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        this.listOfTasks.get(index).mark();
    }

    public void unmarkTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        this.listOfTasks.get(index).unmark();
    }

    public void deleteTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        Task removedTask = this.listOfTasks.remove(index);
        System.out.println("Removed: \n" + removedTask);
    }


    public void printTask() {
        if (listOfTasks.size() == 0) {
            System.out.println(" List of tasks is empty! \n Start adding tasks with: todo, event, deadline");
        }
        for (int i = 0; i < this.listOfTasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + listOfTasks.get(i));
        }
    }

    public void printTask(int index) throws InvalidTaskNumberException {
        validateIndex(index);
        System.out.println(this.listOfTasks.get(index));
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
