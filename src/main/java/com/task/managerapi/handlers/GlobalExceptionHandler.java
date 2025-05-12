package com.task.managerapi.handlers;

import com.task.managerapi.dto.responses.ExceptionResponse;
import com.task.managerapi.exceptions.WrongCredentialsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 424), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handle(AccessDeniedException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 403), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handle(UsernameNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handle(EntityNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<?> handle(WrongCredentialsException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 401), HttpStatus.UNAUTHORIZED);
    }
}