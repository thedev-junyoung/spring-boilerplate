package com.example.springboilerplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class ErrorResponseDTO extends BaseResponseDTO<Map<String, Object>> {
    public ErrorResponseDTO(int httpStatusCode, String message, Map<String, Object> errorDetails) {
        super(httpStatusCode, "error", 0, message, errorDetails);
    }
}