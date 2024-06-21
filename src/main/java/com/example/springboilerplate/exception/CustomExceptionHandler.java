package com.example.springboilerplate.exception;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.utils.ResponseUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final ResponseUtils responseUtils;

    // 생성자 주입
    public CustomExceptionHandler(ResponseUtils responseUtils) {
        this.responseUtils = responseUtils;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return responseUtils.createErrorResponse(ex.getStatusCode(), ex.getReason(), request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return responseUtils.createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors, request.getDescription(false));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(BindException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return responseUtils.createErrorResponse(HttpStatus.BAD_REQUEST, "Binding error", errors, request.getDescription(false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return responseUtils.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid argument", ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return responseUtils.createErrorResponse(HttpStatus.NOT_FOUND, "Element not found", ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return responseUtils.createErrorResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        return responseUtils.createErrorResponse(HttpStatus.CONFLICT, "Data integrity violation", ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex, WebRequest request) {
        return responseUtils.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex, WebRequest request) {
        return responseUtils.createErrorResponse(ex.getStatus(), ex.getMessage(), ex.getDetails(), request.getDescription(false));
    }
}
