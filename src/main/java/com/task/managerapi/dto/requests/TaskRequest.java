package com.task.managerapi.dto.requests;

import com.task.managerapi.enums.TaskStatus;

import java.time.LocalDate;

public record TaskRequest(
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        String ownerId
) {
}