package com.store.handler;

import com.store.exception.*;
import com.store.util.ApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String DELIMITER = ";" + System.lineSeparator();
    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(value = {
            CampaignNotFoundException.class,
            EmailMismatchException.class,
            NotEnoughProductsException.class,
            ProductAlreadyPartOfCampaign.class,
            ProductNotFoundException.class,
            RoleNotFoundException.class,
            UserAlreadyExistException.class,
            UserNotFoundException.class,
            IOException.class,
            MessagingException.class,
            UnsupportedEncodingException.class,
            DisabledException.class,
            BadCredentialsException.class,
            CampaignStartDateAfterEndDateException.class
    })
    public ResponseEntity<?> handleBadRequest(Exception exception){
        LOGGER.error(exception);
        return ResponseEntity.status(BAD_REQUEST).body(getApiError(exception.getMessage(), BAD_REQUEST));
    }

    @ExceptionHandler(value = { ConstraintViolationException.class } )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

//    private String concatViolationMessages(ConstraintViolationException exception) {
//        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
//
//        StringBuilder message = new StringBuilder();
//        for (ConstraintViolation<?> violation : violations) {
//            message.append(violation.getMessage())
//                    .append(DELIMITER);
//        }
//        message.delete(message.lastIndexOf(DELIMITER), message.length());
//        return message.toString();
//    }

    private ApiError getApiError(String message, HttpStatus status) {
        return new ApiError(status, message);
    }
}
