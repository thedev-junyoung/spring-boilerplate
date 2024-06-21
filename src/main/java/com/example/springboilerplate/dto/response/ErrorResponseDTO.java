package com.example.springboilerplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private int statusCode;
    private String errorMessage;
    private Map<String, Object> errorDetails;
}