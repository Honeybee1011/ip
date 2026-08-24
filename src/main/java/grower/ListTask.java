package grower;

import java.util.ArrayList;
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

    public void markTask(int index) {
        this.listOfTasks.get(index).mark();
    }

    public void unmarkTask(int index) {
        this.listOfTasks.get(index).unmark();
    }


    public void printTask() {
        for (int i = 0; i < this.listOfTasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + listOfTasks.get(i));
        }
    }

    public void printTask(int index) {
        System.out.println(this.listOfTasks.get(index));
    }
}