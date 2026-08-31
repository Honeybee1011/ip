package lloyd.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskTest {
    
    @Test
    public void Task_newTask_isNotDone() {
        Task task = new Task("Test task");
        assertFalse(task.isDone());
    }

    @Test
    public void Task_markTask_isDone() {
        Task task = new Task("Test task 2");
        task.mark();
        assertTrue(task.isDone());
    }

    @Test
    public void Task_unmarkTask_isNotDone() {
        Task task = new Task("Test task 3");
        task.mark();
        task.unmark();
        assertFalse(task.isDone());
    }
}
