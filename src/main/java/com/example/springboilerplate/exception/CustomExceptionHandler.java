package com.example.springboilerplate.exception;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.utils.ResponseFactory;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.DuplicateFormatFlagsException;
import java.util.NoSuchElementException;

// 예외 처리를 위한 커스텀 핸들러
@RestControllerAdvice
public class CustomExceptionHandler {

    private final ResponseFactory responseFactory;

    public CustomExceptionHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }
    //
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                (HttpStatus) ex.getStatusCode(),
                ex.getReason(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 요청 데이터 유효성 검증 실패시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldErrors().toString(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 데이터 바인딩 실패시
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(BindException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldErrors().toString(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 인자값 검증 실패시 IllegalArgumentException 예외 발생
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 요소가 없을 때 NoSuchElementException 예외 발생
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 접근 거부시 AccessDeniedException 예외 발생
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 데이터 무결성 위반시
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 모든 예외를 처리하는 핸들러 추가
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 사용자 정의 예외가 발생했을 때
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                (HttpStatus) ex.getStatus(),
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // JWT 토큰 만료시
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "JWT expired: " + ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 중복 포맷 플래그 예외 처리
    @ExceptionHandler(DuplicateFormatFlagsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEDuplicateFormatFlagsException(ExpiredJwtException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.UNAUTHORIZED,
                " " + ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 지원하지 않는 HTTP 메소드 요청시
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method not allowed: " + ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
    // 요청 본문이 읽을 수 없을 때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return responseFactory.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is not readable: " + ex.getMessage(),
                request.getMethod(),
                request.getRequestURI()
        );
    }
}
