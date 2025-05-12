package com.task.managerapi.dto.requests;

public record LoginRequest(
        String username,
        String password
) {
}
