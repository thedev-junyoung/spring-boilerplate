package com.example.springboilerplate.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class CustomException extends RuntimeException {
    private final HttpStatusCode status;
    private final Map<String, Object> details;

    public CustomException(String message, HttpStatusCode status, Map<String, Object> details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
