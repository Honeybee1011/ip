package grower;

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
    
    public void printTask() {
        for (int i = 0; i < numberOfTasks; i++) {
            System.out.println(" " + (i + 1) + ". " + listOfTasks[i]);
        }
    }
}