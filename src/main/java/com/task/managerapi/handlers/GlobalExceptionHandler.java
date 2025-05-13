package com.task.managerapi.handlers;

import com.task.managerapi.dto.responses.ExceptionResponse;
import com.task.managerapi.exceptions.TaskNotFoundException;
import com.task.managerapi.exceptions.UnauthorizedTaskAccessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 424, LocalDateTime.now()), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handle(EntityNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 404, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> handle(TaskNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 404, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedTaskAccessException.class)
    public ResponseEntity<?> handle(UnauthorizedTaskAccessException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 403, LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 400, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}