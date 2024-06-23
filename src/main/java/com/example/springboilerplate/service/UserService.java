package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.user.UpdateUserRequest;
import com.example.springboilerplate.dto.user.UserDTO;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    public List<UserDTO> getAllUsers() {
        logger.info("get all users");
        return userRepository.findAll().stream()
                .map((element) -> modelMapper.map(element, UserDTO.class))
                .collect(Collectors.toList());
    }


    public Optional<User> updateUser(long id, UpdateUserRequest updateRequest) {
        return userRepository.findById(id).map(user -> {
            Optional.ofNullable(updateRequest.getUsername()).ifPresent(user::setUsername);
            // 비밀번호를 먼저 확인하고 암호화
            Optional.ofNullable(updateRequest.getPassword())
                    .filter(password -> !password.isEmpty()) // 빈 문자열이 아닌지 확인
                    .map(bCryptPasswordEncoder::encode) // 비밀번호 암호화
                    .ifPresent(user::setPassword); // 암호화된 비밀번호 설정

            Optional.ofNullable(updateRequest.getProfileImg()).ifPresent(user::setProfileImg);
            return userRepository.save(user);
        });
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }


}