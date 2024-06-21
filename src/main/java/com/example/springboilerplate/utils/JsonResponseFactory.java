package com.example.springboilerplate.utils;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonResponseFactory implements ResponseFactory {

    private final ObjectMapper objectMapper;

    public JsonResponseFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    // SuccessResponseDTO 객체를 생성하고 ResponseEntity 객체로 감싸서 반환
    @Override
    public <T> ResponseEntity<SuccessResponseDTO<T>> createSuccessResponse(T data, String message) {
        SuccessResponseDTO<T> response = new SuccessResponseDTO<>(
                "success",
                1,
                message,
                data
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatus status, String message, Object details) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", details);

        ErrorResponseDTO response = new ErrorResponseDTO(
                "error",
                0,
                status.value(),
                message,
                errorDetails
        );
        return new ResponseEntity<>(response, status);
    }
    // HttpServletResponse 객체에 JSON 응답을 작성
    @Override
    public void writeJsonResponse(HttpServletResponse response, Object responseObject, HttpStatusCode status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        objectMapper.writeValue(response.getWriter(), responseObject);
    }
}
