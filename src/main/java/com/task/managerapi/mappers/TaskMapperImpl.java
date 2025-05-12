package com.task.managerapi.mappers;

import com.task.managerapi.dto.responses.TaskResponse;
import com.task.managerapi.models.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskResponse mapToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getOwnerId()
        );
    }
}