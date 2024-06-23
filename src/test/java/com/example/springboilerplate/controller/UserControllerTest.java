package com.example.springboilerplate.controller;

import com.example.springboilerplate.config.TestSecurityConfig;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.example.springboilerplate.dto.user.UpdateUserRequest;
import com.example.springboilerplate.dto.user.UserDTO;
import com.example.springboilerplate.service.UserService;
import com.example.springboilerplate.type.UserRole;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ResponseFactory responseFactory;

    private UserDTO userDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testuser");
        userDTO.setRole(UserRole.USER);
        userDTO.setProfileImg("profileImg");
        userDTO.setCreatedAt(LocalDateTime.now());

        // Mocking ResponseFactory to return expected response structure
        when(responseFactory.createSuccessResponse(any(), anyString()))
                .thenAnswer(invocation -> {
                    Object data = invocation.getArgument(0);
                    String message = invocation.getArgument(1);
                    return ResponseEntity.ok(new SuccessResponseDTO<>(message, data));
                });
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDTO);

        mockMvc.perform(get("/users/1")
                        .header("Authorization", "Bearer test-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()) // 응답을 콘솔에 출력
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }
    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDTO> userDTOList = List.of(userDTO); // 여기에 필요한 만큼 UserDTO 객체를 추가
        when(userService.getAllUsers()).thenReturn(userDTOList);

        mockMvc.perform(get("/users/")
                        .header("Authorization", "Bearer test-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()) // 응답을 콘솔에 출력
                .andExpect(jsonPath("$.data[0].username").value("testuser")); // 첫 번째 유저의 username 확인
    }

    @Test
    public void testUpdateUser() throws Exception {
        // 테스트 데이터 생성: 업데이트할 유저 정보
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("updatedUser");
        updateUserRequest.setPassword("newPassword");
        updateUserRequest.setProfileImg("newProfileImg");

        // 업데이트된 사용자 데이터 생성: 업데이트된 사용자 정보를 포함하는 UserDTO 객체
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUserId(1L);
        updatedUserDTO.setEmail("test@example.com");
        updatedUserDTO.setUsername("updatedUser");
        updatedUserDTO.setRole(UserRole.USER);
        updatedUserDTO.setProfileImg("newProfileImg");
        updatedUserDTO.setCreatedAt(LocalDateTime.now());

        // 서비스 계층 모킹: userService.updateUser 메서드가 업데이트된 사용자 정보를 반환하도록 설정
        when(userService.updateUser(anyLong(), any(UpdateUserRequest.class))).thenReturn(updatedUserDTO);

        // 업데이트 요청
        mockMvc.perform(put("/users/1")
                        .header("Authorization", "Bearer test-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"updatedUser\", \"password\": \"newPassword\", \"profileImg\": \"newProfileImg\"}"))
                .andExpect(status().isOk()) // HTTP 상태 코드 200 확인
                .andDo(MockMvcResultHandlers.print()) // 응답을 콘솔에 출력
                .andExpect(jsonPath("$.data.username").value("updatedUser")); // 업데이트된 유저의 username 확인
    }

    @Test
    public void testDeleteUser() throws Exception {
        // userService.deleteUser 메서드가 void를 반환하므로 예외가 발생하지 않으면 성공
        mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer test-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()) // 응답을 콘솔에 출력
                .andExpect(jsonPath("$.message").value("User deleted successfully")); // 성공 메시지 확인
    }
}
