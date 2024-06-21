package com.example.springboilerplate.exception;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.utils.JsonResponseFactory;
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

    private final JsonResponseFactory responseFactory;

    // 생성자 주입
    public CustomExceptionHandler(JsonResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        return responseFactory.createErrorResponse(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("path", request.getDescription(false));
        return responseFactory.createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(BindException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("path", request.getDescription(false));
        return responseFactory.createErrorResponse(HttpStatus.BAD_REQUEST, "Binding error", errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        details.put("error", ex.getMessage());
        return responseFactory.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid argument", details);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        details.put("error", ex.getMessage());
        return responseFactory.createErrorResponse(HttpStatus.NOT_FOUND, "Element not found", details);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        details.put("error", ex.getMessage());
        return responseFactory.createErrorResponse(HttpStatus.FORBIDDEN, "Access denied", details);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        details.put("error", ex.getMessage());
        return responseFactory.createErrorResponse(HttpStatus.CONFLICT, "Data integrity violation", details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>();
        details.put("path", path);
        details.put("error", ex.getMessage());
        return responseFactory.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", details);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex, WebRequest request) {
        String path = request.getDescription(false);
        Map<String, Object> details = new HashMap<>(ex.getDetails());
        details.put("path", path);
        return responseFactory.createErrorResponse((HttpStatus) ex.getStatus(), ex.getMessage(), details);
    }
}
