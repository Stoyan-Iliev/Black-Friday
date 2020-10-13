package com.store.util;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String type;
    private String message;

    public ApiError(HttpStatus status, String type, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.type = type;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
