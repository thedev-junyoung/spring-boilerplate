package com.example.springboilerplate.controller;

import com.example.springboilerplate.config.TestSecurityConfig;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.example.springboilerplate.dto.user.JoinRequest;
import com.example.springboilerplate.service.JoinService;
import com.example.springboilerplate.utils.ResponseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MainController.class)
@Import(TestSecurityConfig.class) // 테스트 환경에서의 보안 설정 무력화
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JoinService joinService;

    @MockBean
    private ResponseFactory responseFactory;

    @InjectMocks
    private MainController mainController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mocking ResponseFactory to return expected response structure
        when(responseFactory.createSuccessResponse(null, "join complete!"))
                .thenAnswer(invocation -> ResponseEntity.ok(new SuccessResponseDTO<>(null, "join complete!")));
    }

    @Test
    public void testGetMain() throws Exception {
        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()) // 응답을 콘솔에 출력
                .andExpect(content().string("Hello. boilerplate!"));
    }

    @Test
    public void testJoin() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail("test@example.com");
        joinRequest.setPassword("password123");

        doNothing().when(joinService).join(joinRequest);

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()) // 응답을 콘솔에 출력
                .andExpect(jsonPath("$.data").value("join complete!"));
    }
}
