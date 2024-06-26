package com.example.springboilerplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO<T> {
    private int httpStatusCode;
    private String status;
    private int code;
    private String message;
    private T data;
}


