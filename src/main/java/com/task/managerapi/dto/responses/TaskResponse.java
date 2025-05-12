package com.task.managerapi.dto.responses;

import com.task.managerapi.enums.TaskStatus;

import java.time.LocalDate;

public record TaskResponse(
        Long taskId,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        String ownerId
) {
}