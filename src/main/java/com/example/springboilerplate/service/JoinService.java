package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.user.JoinRequest;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void join(JoinRequest joinRequest){
        if (userRepository.existsByEmail(joinRequest.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일 입니다.");
        }
        User user = new User();
        user.setUsername(joinRequest.getUsername());
        user.setEmail(joinRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        userRepository.save(user);
    }
}
