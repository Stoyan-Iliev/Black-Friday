package com.store.handler;

import com.store.exception.ProductNotFoundException;
import com.store.util.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String DELIMITER = ";" + System.lineSeparator();

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> constraintExceptionHandler(ConstraintViolationException exception) {
        String message = concatViolationMessages(exception);
        return new ResponseEntity<>(getApiError(message, BAD_REQUEST), BAD_REQUEST);
    }

    private String concatViolationMessages(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage())
                    .append(DELIMITER);
        }
        return message.toString();
    }

    private ApiError getApiError(String message, HttpStatus status) {
        return new ApiError(status, "validation", message);
    }

    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<?> noSuchElementExceptionHandler(ProductNotFoundException exception){
        return new ResponseEntity<>(getApiError(exception.getMessage(), BAD_REQUEST), BAD_REQUEST);
    }
}