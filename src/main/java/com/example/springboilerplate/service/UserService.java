package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.user.UpdateUserRequest;
import com.example.springboilerplate.dto.user.UserDTO;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.exception.CustomException;
import com.example.springboilerplate.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional; // 스프링 트랜잭션 애너테이션 사용
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public UserDTO getUserById(Long id) {
        User user = findUserById(id);
        return modelMapper.map(user, UserDTO.class);
    }
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public List<UserDTO> getAllUsers() {
        logger.info("get all users");
        return userRepository.findAll().stream()
                .map((element) -> modelMapper.map(element, UserDTO.class))
                .collect(Collectors.toList());
    }
    @Transactional // 트랜잭션 처리
    public UserDTO updateUser(long id, UpdateUserRequest updateRequest) {
        User user = findUserById(id);

        Optional.ofNullable(updateRequest.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(updateRequest.getPassword())
                .filter(password -> !password.isEmpty())
                .map(bCryptPasswordEncoder::encode)
                .ifPresent(user::setPassword);
        Optional.ofNullable(updateRequest.getProfileImg()).ifPresent(user::setProfileImg);

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
    @Transactional // 트랜잭션 처리
    public void deleteUser(long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> { // orElseThrow: 해당 값이 존재하지 않을 때 예외를 발생시킴
                    Map<String, Object> errorDetails = new HashMap<>();
                    errorDetails.put("userId", id);
                    errorDetails.put("error", "User not found");
                    return new CustomException("User not found with id: " + id, HttpStatus.NOT_FOUND, errorDetails);
                });
    }
}