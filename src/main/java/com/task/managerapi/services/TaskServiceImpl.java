package com.task.managerapi.services;

import com.task.managerapi.dto.requests.TaskRequest;
import com.task.managerapi.dto.responses.PagedResponse;
import com.task.managerapi.dto.responses.TaskResponse;
import com.task.managerapi.exceptions.TaskNotFoundException;
import com.task.managerapi.mappers.PagedMapper;
import com.task.managerapi.mappers.TaskMapper;
import com.task.managerapi.models.Task;
import com.task.managerapi.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final PagedMapper pagedMapper;
    private final SecurityLayerService securityLayerService;

    @Override
    @Transactional
    public void createTask(TaskRequest taskRequest) {
        taskRepository.save(taskMapper.mapToTask(taskRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TaskResponse> getAllTasks(int pageNo, int pageSize) {
        pageNo = Math.max(pageNo, 1);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").descending());
        Page<Task> taskPage = taskRepository.findAllByOwnerId(pageable, getOwnerId());

        List<TaskResponse> userResponseList = taskPage.getContent()
                .stream()
                .map(taskMapper::mapToTaskResponse)
                .collect(Collectors.toList());

        return pagedMapper.mapToPagedResponse(userResponseList, taskPage);
    }

    @Override
    public TaskResponse getTaskById(long id) {
        Task task = taskRepository.findByIdAndOwnerId(id, getOwnerId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

        return taskMapper.mapToTaskResponse(task);
    }

    @Override
    @Transactional
    public void updateTask(long id, TaskRequest taskRequest) {
        Task task = taskRepository.findByIdAndOwnerId(id, getOwnerId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setStatus(taskRequest.status());
        task.setDueDate(taskRequest.dueDate());
    }

    @Override
    @Transactional
    public void deleteTaskById(long id) {
        taskRepository.deleteById(getTaskById(id).taskId());
    }

    private String getOwnerId() {
        return securityLayerService.getUserFromToken().ownerId();
    }
}