package com.task.managerapi.controllers;

import com.task.managerapi.dto.requests.TaskRequest;
import com.task.managerapi.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyRole('role_user', 'role_admin')")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
        taskService.createTask(taskRequest);
        return new ResponseEntity<>("Task created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('role_user', 'role_admin')")
    public ResponseEntity<?> getAllTasks(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(taskService.getAllTasks(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('role_user', 'role_admin')")
    public ResponseEntity<?> getTaskById(@PathVariable long id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('role_user', 'role_admin')")
    public ResponseEntity<?> deleteTaskById(@PathVariable long id) {
        taskService.deleteTaskById(id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }
}