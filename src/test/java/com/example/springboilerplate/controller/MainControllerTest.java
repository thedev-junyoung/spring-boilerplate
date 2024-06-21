package com.example.springboilerplate.controller;

import com.example.springboilerplate.dto.user.JoinRequest;
import com.example.springboilerplate.service.JoinService;
import com.example.springboilerplate.utils.ResponseFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JoinService joinService;

    @MockBean
    private ResponseFactory responseFactory;

    @Test
    void testJoin() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail("test@example.com");
        joinRequest.setPassword("password");
        joinRequest.setPassword("username");

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"success\",\"code\":1,\"data\":null,\"message\":\"join complete!\"}"));
    }
}
