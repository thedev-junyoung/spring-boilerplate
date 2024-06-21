package com.example.springboilerplate.controller;

import com.example.springboilerplate.config.TestSecurityConfig;
import com.example.springboilerplate.dto.user.JoinRequest;
import com.example.springboilerplate.service.JoinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MainController.class)
@Import(TestSecurityConfig.class) // Include the TestConfig
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JoinService joinService;

    @Test
    void testJoin() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail("test@example.com");
        joinRequest.setPassword("password");
        joinRequest.setUsername("username");

        doNothing().when(joinService).join(any(JoinRequest.class));

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //.with(SecurityMockMvcRequestPostProcessors.csrf()) // CSRF 토큰 추가
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"success\",\"code\":1,\"data\":null,\"message\":\"join complete!\"}"));

    }


    @Test
    void testJoin_EmailAlreadyExists() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail("test@example.com");
        joinRequest.setPassword("password");
        joinRequest.setUsername("username");

        // doThrow: 예외를 발생시키는 메서드
        doThrow(new IllegalArgumentException("Email already exists"))
                .when(joinService).join(any(JoinRequest.class));
        // when: joinService.join(joinRequest) 메서드가 호출될 때 예외를 발생시키도록 설정

        mockMvc.perform(post("/join")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Email already exists"));
    }
}
