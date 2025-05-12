package com.task.managerapi.mappers;

import com.task.managerapi.dto.requests.TaskRequest;
import com.task.managerapi.dto.responses.TaskResponse;
import com.task.managerapi.models.Task;

public interface TaskMapper {
    Task mapToTask(TaskRequest taskRequest);

    TaskResponse mapToTaskResponse(Task task);
}