package com.task.managerapi.mappers;

import com.task.managerapi.dto.responses.TaskResponse;
import com.task.managerapi.models.Task;

public interface TaskMapper {
    TaskResponse mapToTaskResponse(Task task);
}
