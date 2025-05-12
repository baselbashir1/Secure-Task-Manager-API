//package com.task.managerapi.handlers;
//
//import com.ehr.userservice.dto.responses.ValidationExceptionResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class ValidationExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
//        Map<String, String> errors = new HashMap<>();
//
//        e.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage())
//        );
//
//        return new ResponseEntity<>(new ValidationExceptionResponse(errors, 400), HttpStatus.BAD_REQUEST);
//    }
//
//}