package com.store.exception;

public class EmailMismatchException extends RuntimeException {
    public EmailMismatchException(String message) {
        super(message);
    }
}
