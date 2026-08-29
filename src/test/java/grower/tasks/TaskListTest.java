package grower.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import grower.growerExceptions.InvalidTaskNumberException;

public class TaskListTest {
    @Test
    public void markTask_validIndex_marksAndReturnsSelectedTask() throws InvalidTaskNumberException {
        TaskList tasks = createTwoTaskList();

        Task markedTask = tasks.markTask(1);

        assertSame(tasks.getTasks().get(1), markedTask);
        assertFalse(tasks.getTasks().get(0).isCompleted());
        assertTrue(markedTask.isCompleted());
    }

    @Test
    public void unmarkTask_validIndex_unmarksAndReturnsSelectedTask() throws InvalidTaskNumberException {
        TaskList tasks = createTwoTaskList();
        Task task = tasks.markTask(0);

        Task unmarkedTask = tasks.unmarkTask(0);

        assertSame(task, unmarkedTask);
        assertFalse(unmarkedTask.isCompleted());
    }

    @Test
    public void deleteTask_validIndex_removesAndReturnsSelectedTask() throws InvalidTaskNumberException {
        TaskList tasks = createTwoTaskList();
        Task expectedDeletedTask = tasks.getTasks().get(0);

        Task deletedTask = tasks.deleteTask(0);

        assertSame(expectedDeletedTask, deletedTask);
        assertEquals(1, tasks.getTasks().size());
        assertEquals("second", tasks.getTasks().getFirst().getDescription());
    }

    @Test
    public void taskMutation_indexOutsideList_invalidTaskNumberExceptionThrown() {
        TaskList tasks = createTwoTaskList();

        assertThrows(InvalidTaskNumberException.class, () -> tasks.markTask(-1));
        assertThrows(InvalidTaskNumberException.class, () -> tasks.markTask(2));
        assertThrows(InvalidTaskNumberException.class, () -> tasks.unmarkTask(2));
        assertThrows(InvalidTaskNumberException.class, () -> tasks.deleteTask(2));
    }

    @Test
    public void taskMutation_emptyList_invalidTaskNumberExceptionThrown() {
        TaskList tasks = new TaskList();

        InvalidTaskNumberException exception = assertThrows(
                InvalidTaskNumberException.class,
                () -> tasks.markTask(0));

        assertEquals("There are no tasks in the list.", exception.getMessage());
    }

    @Test
    public void getTasks_returnsUnmodifiableSnapshotInInsertionOrder() {
        TaskList tasks = createTwoTaskList();
        List<Task> snapshot = tasks.getTasks();

        assertEquals("first", snapshot.get(0).getDescription());
        assertEquals("second", snapshot.get(1).getDescription());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new ToDo("third")));

        tasks.addTask(new ToDo("third"));
        assertEquals(2, snapshot.size());
        assertEquals(3, tasks.getTasks().size());
    }

    @Test
    public void getTaskData_mixedTasks_returnsSerializedDataInInsertionOrder()
            throws InvalidTaskNumberException {
        TaskList tasks = new TaskList();
        tasks.addTask(new ToDo("read book"));
        tasks.addTask(new Deadline(
                "submit work",
                LocalDateTime.of(2026, 8, 31, 23, 59)));
        tasks.markTask(1);

        assertEquals(
                List.of(
                        "T | 0 | read book",
                        "D | 1 | submit work | 2026-08-31T23:59:00"),
                tasks.getTaskData());
    }

    private TaskList createTwoTaskList() {
        TaskList tasks = new TaskList();
        tasks.addTask(new ToDo("first"));
        tasks.addTask(new ToDo("second"));
        return tasks;
    }
}
