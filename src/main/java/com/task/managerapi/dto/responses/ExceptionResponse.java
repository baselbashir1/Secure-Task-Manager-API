package com.task.managerapi.dto.responses;

public record ExceptionResponse(
        String message,
        int statusCode
) {
}
