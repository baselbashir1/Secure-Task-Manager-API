package com.task.managerapi.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnauthorizedTaskAccessException extends RuntimeException {
    private final String message;
}