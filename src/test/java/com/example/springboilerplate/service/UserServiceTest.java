package com.example.springboilerplate.service;

import com.example.springboilerplate.config.TestSecurityConfig;
import com.example.springboilerplate.dto.user.UpdateUserRequest;
import com.example.springboilerplate.dto.user.UserDTO;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.exception.CustomException;
import com.example.springboilerplate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// @ExtendWith: JUnit5에서 Mockito를 사용하여 테스트 클래스에 확장(Extension) 기능을 추가하여, Mockito의 기능을 사용할 수 있게 합니다.
@SpringBootTest // @SpringBootTest: 테스트 시 전체 스프링 애플리케이션 컨텍스트를 로드하여 통합 테스트를 수행할 수 있게 합니다.
@Import(TestSecurityConfig.class)
// @Import: 테스트 환경에서 특정 설정 클래스(TestSecurityConfig)를 가져와 테스트 시 보안 설정 등을 무력화하거나 대체합니다.

public class UserServiceTest {

    @Autowired // @Autowired: 테스트 대상이 되는 Bean을 주입
    private UserService userService;

    @MockBean // MockBean: 테스트 환경에서 사용할 Mock 객체를 Bean으로 등록
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    private ModelMapper modelMapper;


    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this): @Mock, @InjectMocks, @Spy, @Captor, @MockBean, @SpyBean 애노테이션을 사용하여 생성한 Mock 객체를 초기화
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setProfileImg("profileImg");

        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUsername("testuser");
        userDTO.setProfileImg("profileImg");
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(userDTO.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(user, UserDTO.class);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomException.class, () -> userService.getUserById(2L));

        String expectedMessage = "User not found with id: 2";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).findById(2L);
    }
    @Test
    void testGetAllUsers() {
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        anotherUser.setUsername("anotheruser");

        List<User> users = Arrays.asList(user, anotherUser);
        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(anotherUser, UserDTO.class)).thenReturn(new UserDTO());

        List<UserDTO> userDTOs = userService.getAllUsers();

        assertNotNull(userDTOs);
        assertEquals(2, userDTOs.size());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(User.class), eq(UserDTO.class));
    }



}
