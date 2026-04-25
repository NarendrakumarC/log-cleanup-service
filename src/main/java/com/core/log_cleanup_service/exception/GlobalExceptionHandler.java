package com.core.log_cleanup_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<String> handleCleanUp(CleanUpException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
