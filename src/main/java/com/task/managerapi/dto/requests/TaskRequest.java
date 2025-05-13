package com.task.managerapi.dto.requests;

import com.task.managerapi.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record TaskRequest(
        @NonNull @NotBlank String title,
        @Nullable String description,
        @Nullable TaskStatus status,
        @NonNull LocalDate dueDate,
        @Nullable String ownerId
) {
}