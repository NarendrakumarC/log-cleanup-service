package com.core.log_cleanup_service.exception;

public class CleanUpException extends RuntimeException {
    public CleanUpException(String message) {
        super(message);
    }

    public CleanUpException(String message, Throwable cause) {
        super(message, cause);
    }
}
