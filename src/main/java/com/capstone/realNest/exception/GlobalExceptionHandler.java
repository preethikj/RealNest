package com.capstone.realNest.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), errors);
    }

    private ApiError buildError(HttpStatus status, String message, String path, Map<String, String> validationErrors) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validationErrors
        );
    }
}
