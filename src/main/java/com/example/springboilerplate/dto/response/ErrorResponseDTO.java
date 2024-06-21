package com.example.springboilerplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String status = "error";
    private int code = 0;
    private int statusCode;
    private String message;
    private Map<String, Object> errorDetails;
}