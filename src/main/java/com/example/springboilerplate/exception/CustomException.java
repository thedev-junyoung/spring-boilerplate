package com.example.springboilerplate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatusCode status;
    private final Map<String, Object> details;

    public CustomException(String message, HttpStatusCode status, Map<String, Object> details) {
        super(message);
        this.status = status;
        this.details = details;
    }

}
