package com.example.springboilerplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SuccessResponseDTO<T> extends BaseResponseDTO<T> {
    public SuccessResponseDTO(String message, T data) {
        super(200, "success", 1, message, data);
    }
}
