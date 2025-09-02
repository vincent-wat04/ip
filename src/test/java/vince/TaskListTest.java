package vince;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import java.nio.file.Path;
import java.util.List;

import vince.storage.TaskList;
import vince.task.Task;

public class TaskListTest {

    @TempDir
    Path tempDir;

    private TaskList tasks;

    @BeforeEach
    void setup() {
        tasks = new TaskList();
        tasks.deleteAll();
    }

    @Test
    @DisplayName("add todo/deadline/event and list returns proper lines")
    void add_and_list() {
        Task t1 = tasks.addTask("todo read book");
        Task t2 = tasks.addTask("deadline return book /by 15/12/2024 1800");
        Task t3 = tasks.addTask("event project meeting /from 20/12/2024 1400 /to 20/12/2024 1600");

        List<String> lines = tasks.list();
        Assertions.assertEquals(3, lines.size());
        Assertions.assertTrue(lines.get(0).contains(t1.toString()));
        Assertions.assertTrue(lines.get(1).contains(t2.toString()));
        Assertions.assertTrue(lines.get(2).contains(t3.toString()));
    }

    @Test
    @DisplayName("tasksOnDateLines filters deadlines and events by date")
    void tasks_on_date_lines() {
        tasks.addTask("todo read book");
        tasks.addTask("deadline return book /by 15/12/2024 1800");
        tasks.addTask("event project meeting /from 20/12/2024 1400 /to 20/12/2024 1600");

        List<String> lines15 = tasks.tasksOnDateLines("15/12/2024");
        Assertions.assertEquals(1, lines15.size());
        Assertions.assertTrue(lines15.get(0).contains("return book"));

        List<String> lines20 = tasks.tasksOnDateLines("20/12/2024");
        Assertions.assertEquals(1, lines20.size());
        Assertions.assertTrue(lines20.get(0).contains("project meeting"));
    }
}
