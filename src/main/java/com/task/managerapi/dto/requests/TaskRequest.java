package com.task.managerapi.dto.requests;

import java.time.LocalDate;

public record TaskRequest(
        String title,
        String description,
        LocalDate dueDate
) {
}