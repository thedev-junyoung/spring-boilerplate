package com.example.springboilerplate.controller;


import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.example.springboilerplate.dto.user.UpdateUserRequest;
import com.example.springboilerplate.dto.user.UserDTO;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.exception.CustomException;
import com.example.springboilerplate.service.UserService;
import com.example.springboilerplate.utils.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")  // 모든 메소드를 /users 경로 아래로 이동
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ResponseFactory responseFactory;
    public UserController(UserService userService, ResponseFactory responseFactory) {
        this.userService = userService;
        this.responseFactory = responseFactory;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return responseFactory.createSuccessResponse(userDTO, "User found successfully");

    }


    @GetMapping("/")
    public ResponseEntity<SuccessResponseDTO<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return responseFactory.createSuccessResponse(users, "모든 유저 조회 완료");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateRequest) {
        UserDTO updatedUser = userService.updateUser(id, updateRequest);
        return responseFactory.createSuccessResponse(updatedUser, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return responseFactory.createSuccessResponse(null, "User deleted successfully");
    }
}