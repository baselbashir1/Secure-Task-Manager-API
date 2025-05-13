package com.task.managerapi.dto.requests;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

public record LoginRequest(
        @NonNull @NotBlank String username,
        @NonNull @NotBlank String password
) {
}