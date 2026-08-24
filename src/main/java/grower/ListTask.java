package grower;

import grower.tasks.Task;

public class ListTask {
    private static Task[] listOfTasks;
    private static int numberOfTasks;

    public ListTask() {
        listOfTasks = new Task[100];
        numberOfTasks = 0;
    }

    public boolean addTask(Task newTask) {
        if (numberOfTasks < 100) {
            ListTask.listOfTasks[numberOfTasks] = newTask;
            System.out.println(" added: " + ListTask.listOfTasks[numberOfTasks]);
            ListTask.numberOfTasks++;
            return true;
        } else {
            return false;
        }
    }

    public void markTask(int index) {
        ListTask.listOfTasks[index].mark();
    }

    public void unmarkTask(int index) {
        ListTask.listOfTasks[index].unmark();
    }

    public void printTask() {
        for (int i = 0; i < numberOfTasks; i++) {
            System.out.println(" " + (i + 1) + ". " + listOfTasks[i]);
        }
    }

    public void printTask(int index) {
        System.out.println(ListTask.listOfTasks[index]);
    }
}