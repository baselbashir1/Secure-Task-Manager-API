package com.task.managerapi.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InactiveAccountException extends RuntimeException {
    private final String message;
}