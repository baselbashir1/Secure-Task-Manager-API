package com.task.managerapi.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisabledAccountException extends RuntimeException {
    private final String message;
}