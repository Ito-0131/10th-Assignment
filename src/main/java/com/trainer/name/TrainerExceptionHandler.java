package com.trainer.name;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TrainerExceptionHandler {
    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<String> handleTrainerNotFoundException(TrainerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
