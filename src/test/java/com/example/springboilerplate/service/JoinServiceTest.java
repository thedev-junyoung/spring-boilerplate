package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.user.JoinRequest;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JoinServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private JoinService joinService;

    @BeforeEach
    void setUp() {
        // 위에 생성된 mock 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void join_ShouldSaveUser_WhenEmailIsNotRegistered() {
        // Arrange
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUsername("testuser");
        joinRequest.setEmail("test@example.com");
        joinRequest.setPassword("password");

        when(userRepository.existsByEmail(joinRequest.getEmail())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(joinRequest.getPassword())).thenReturn("encodedPassword");

        // Act
        joinService.join(joinRequest);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void join_ShouldThrowException_WhenEmailIsAlreadyRegistered() {
        // Arrange
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUsername("testuser");
        joinRequest.setEmail("test@example.com");
        joinRequest.setPassword("password");

        when(userRepository.existsByEmail(joinRequest.getEmail())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> joinService.join(joinRequest),
                "Expected join() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Email already exists"));
        verify(userRepository, never()).save(any());
    }
    @Test
    public void join_ShouldThrowIllegalArgumentException_WhenEmailAlreadyExists() {
        // Given
        String email = "test@example.com";
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail(email);
        joinRequest.setPassword("password");
        joinRequest.setUsername("username");

        // Mock the behavior of userRepository.existsByEmail to return true
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            joinService.join(joinRequest);
        });
    }

}
