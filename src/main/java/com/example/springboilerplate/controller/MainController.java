package com.example.springboilerplate.controller;

import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.example.springboilerplate.dto.user.JoinRequest;
import com.example.springboilerplate.service.JoinService;
import com.example.springboilerplate.utils.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final ResponseFactory responseFactory;

    private final JoinService joinService;

    public MainController(ResponseFactory responseFactory, JoinService joinService) {
        this.responseFactory = responseFactory;
        this.joinService = joinService;
    }

    // join, login, logout,
    @GetMapping("/")
    public String getMain() {
        return "Hello. boilerplate!";
    }

    @PostMapping("/join")
    public ResponseEntity<SuccessResponseDTO<Void>> join(@RequestBody JoinRequest joinRequest) {
        logger.info("Received join request for email: {}", joinRequest.getEmail());
        joinService.join(joinRequest);
        return responseFactory.createSuccessResponse(null, "join complete!");
    }
}
