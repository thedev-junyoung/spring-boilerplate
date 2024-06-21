package com.example.springboilerplate.utils;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseUtils {

    private final ObjectMapper objectMapper;

    public ResponseUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> ResponseEntity<SuccessResponseDTO<T>> createSuccessResponse(T data, String message) {
        SuccessResponseDTO<T> response = new SuccessResponseDTO<>(
                "success",
                1,
                data,
                message
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatusCode status, String message, Object details) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", details);

        ErrorResponseDTO response = new ErrorResponseDTO(
                status.value(),
                message,
                errorDetails
        );
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatusCode status, String message, Map<String, Object> details, String path) {
        details.put("path", path);
        ErrorResponseDTO response = new ErrorResponseDTO(
                status.value(),
                message,
                details
        );
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<ErrorResponseDTO> createErrorResponse(HttpStatusCode status, String message, String details, String path) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", details);
        errorDetails.put("path", path);

        ErrorResponseDTO response = new ErrorResponseDTO(
                status.value(),
                message,
                errorDetails
        );
        return new ResponseEntity<>(response, status);
    }

    public void writeJsonResponse(HttpServletResponse response, Object responseObject, HttpStatusCode status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        objectMapper.writeValue(response.getWriter(), responseObject);
    }
}
