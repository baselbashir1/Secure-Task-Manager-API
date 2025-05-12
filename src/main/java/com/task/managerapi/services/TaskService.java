package com.task.managerapi.services;

import com.task.managerapi.dto.requests.TaskRequest;
import com.task.managerapi.dto.responses.PagedResponse;
import com.task.managerapi.dto.responses.TaskResponse;

public interface TaskService {
    void createTask(TaskRequest taskRequest);

    PagedResponse<TaskResponse> getAllTasks(int pageNo, int pageSize);

    TaskResponse getTaskById(long id);

    void deleteTaskById(long id);
}