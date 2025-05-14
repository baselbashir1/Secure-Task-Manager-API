package com.task.managerapi;

import com.task.managerapi.controllers.TaskController;
import com.task.managerapi.dto.requests.TaskRequest;
import com.task.managerapi.enums.TaskStatus;
import com.task.managerapi.services.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        log.info("TaskControllerTest setUp");
        taskRequest = new TaskRequest(
                "Test Task",
                "Test Description",
                TaskStatus.PENDING,
                LocalDate.now(),
                null
        );
    }

    @Test
    @WithMockUser(roles = {"user"})
    void createTask() {
        log.info("TaskControllerTest createTask");
        ResponseEntity<?> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Task created successfully", response.getBody());
        verify(taskService, times(1)).createTask(taskRequest);
    }

    @Test
    @WithMockUser(roles = {"user"})
    void getAllTasks() {
        log.info("TaskControllerTest getAllTasks");
        int pageNo = 1;
        int pageSize = 10;
        when(taskService.getAllTasks(pageNo, pageSize)).thenReturn(taskService.getAllTasks(pageNo, pageSize));

        ResponseEntity<?> response = taskController.getAllTasks(pageNo, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mocked task list", response.getBody());
        verify(taskService, times(1)).getAllTasks(pageNo, pageSize);
    }

    @Test
    @WithMockUser(roles = {"user"})
    void getTaskById() {
        log.info("TaskControllerTest getTaskById");
        long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(taskService.getTaskById(taskId));

        ResponseEntity<?> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mocked task", response.getBody());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    @WithMockUser(roles = {"user"})
    void updateTask() {
        log.info("TaskControllerTest updateTask");
        long taskId = 1L;

        ResponseEntity<?> response = taskController.updateTask(taskId, taskRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task updated successfully", response.getBody());
        verify(taskService, times(1)).updateTask(taskId, taskRequest);
    }

    @Test
    @WithMockUser(roles = {"user"})
    void deleteTask() {
        log.info("TaskControllerTest deleteTask");
        long taskId = 1L;

        ResponseEntity<?> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task deleted successfully", response.getBody());
        verify(taskService, times(1)).deleteTaskById(taskId);
    }
}