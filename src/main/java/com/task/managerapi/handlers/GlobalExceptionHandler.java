//package com.task.managerapi.handlers;
//
//import com.ehr.userservice.dto.responses.ExceptionResponse;
//import com.ehr.userservice.exceptions.AccountLockedException;
//import com.ehr.userservice.exceptions.DisabledAccountException;
//import com.ehr.userservice.exceptions.InactiveAccountException;
//import com.ehr.userservice.exceptions.WrongCredentialsException;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handle(Exception e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 424), HttpStatus.FAILED_DEPENDENCY);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Object> handle(AccessDeniedException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 403), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<Object> handle(UsernameNotFoundException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 404), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Object> handle(EntityNotFoundException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 404), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(WrongCredentialsException.class)
//    public ResponseEntity<Object> handle(WrongCredentialsException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 401), HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(DisabledAccountException.class)
//    public ResponseEntity<Object> handle(DisabledAccountException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 403), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(InactiveAccountException.class)
//    public ResponseEntity<Object> handle(InactiveAccountException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 403), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(AccountLockedException.class)
//    public ResponseEntity<Object> handle(AccountLockedException e) {
//        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), 423), HttpStatus.LOCKED);
//    }
//
//}