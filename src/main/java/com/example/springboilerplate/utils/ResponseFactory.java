package com.example.springboilerplate.utils;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ResponseFactory {

    // 성공 응답을 생성하는 메서드
    <T> ResponseEntity<SuccessResponseDTO<T>> createSuccessResponse(T data, String message);

    // 오류 응답을 생성하는 메서드
    ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatus status, String errorMessage, String method, String path);

    // JSON 응답을 작성하는 메서드
    void writeJsonResponse(HttpServletResponse response, Object responseObject, HttpStatusCode status) throws IOException;
}
