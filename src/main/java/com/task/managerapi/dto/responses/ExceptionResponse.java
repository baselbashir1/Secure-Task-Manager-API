package com.task.managerapi.dto.responses;

import java.time.LocalDateTime;

public record ExceptionResponse(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}