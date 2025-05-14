package com.task.managerapi.dto.responses;

public record UserResponse(
        String userId,
        String username,
        String email
) {
}