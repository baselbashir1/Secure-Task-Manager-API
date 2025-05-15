package com.task.managerapi;

import com.task.managerapi.enums.TaskStatus;
import com.task.managerapi.models.Task;
import com.task.managerapi.repositories.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskUnitTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveTask() {
        System.out.println("saveTask");
        Task task = Task.builder()
                .title("basel")
                .description("basel")
                .status(TaskStatus.COMPLETED)
                .dueDate(LocalDate.now())
                .ownerId("basel")
                .build();

        taskRepository.save(task);

        System.out.println(task);
        Assertions.assertThat(task.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getTask() {
        System.out.println("getTask");
        Task task = taskRepository.findById(1L).get();

        System.out.println(task);
        Assertions.assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getAllTasks() {
        System.out.println("getAllTasks");
        List<Task> tasks = taskRepository.findAll();

        System.out.println(tasks);
        Assertions.assertThat(tasks.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateTask() {
        System.out.println("updateTask");
        Task task = taskRepository.findById(1L).get();
        task.setTitle("basel");
        Task taskUpdated = taskRepository.save(task);

        System.out.println(taskUpdated);
        Assertions.assertThat(taskUpdated.getTitle()).isEqualTo("basel");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteTask() {
        System.out.println("deleteTask");

        taskRepository.deleteById(1L);
        Optional<Task> task = taskRepository.findById(1L);

        Assertions.assertThat(task).isEmpty();
    }
}