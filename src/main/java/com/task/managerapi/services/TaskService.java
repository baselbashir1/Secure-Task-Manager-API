package com.task.managerapi.services;

import com.task.managerapi.dto.responses.PagedResponse;
import com.task.managerapi.dto.responses.TaskResponse;

public interface TaskService {
    PagedResponse<TaskResponse> getAllTasks(int pageNo, int pageSize);
}