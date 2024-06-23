package com.example.springboilerplate.exception;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.utils.ResponseFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final ResponseFactory responseFactory;

    public CustomExceptionHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                (HttpStatus) ex.getStatusCode(),
                ex.getReason(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldErrors().toString(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(BindException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldErrors().toString(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                (HttpStatus) ex.getStatus(),
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
}
