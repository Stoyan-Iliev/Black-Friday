package com.store.exception;

public class ImageUploaderServiceException extends RuntimeException {
    public ImageUploaderServiceException(String message) {
    }

    public ImageUploaderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
