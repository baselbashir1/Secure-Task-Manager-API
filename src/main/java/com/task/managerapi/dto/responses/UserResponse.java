package com.task.managerapi.dto.responses;

public record UserResponse(
        String ownerId,
        String username,
        String email
) {
}