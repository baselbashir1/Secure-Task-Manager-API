package com.task.managerapi.mappers;

import com.task.managerapi.dto.requests.TaskRequest;
import com.task.managerapi.dto.responses.TaskResponse;
import com.task.managerapi.enums.TaskStatus;
import com.task.managerapi.models.Task;
import com.task.managerapi.services.SecurityLayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskMapperImpl implements TaskMapper {

    private final SecurityLayerService securityLayerService;

    @Override
    public Task mapToTask(TaskRequest taskRequest) {
        return new Task(
                taskRequest.title(),
                taskRequest.description(),
                TaskStatus.PENDING,
                taskRequest.dueDate(),
                securityLayerService.getUserFromToken().ownerId()
        );
    }

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