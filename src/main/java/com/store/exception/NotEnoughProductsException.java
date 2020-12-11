package com.store.exception;

public class NotEnoughProductsException extends RuntimeException {
    public NotEnoughProductsException(String message) {
        super(message);
    }
}
