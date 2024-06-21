package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.user.AppUserDetails;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(LoginService.class);
    public LoginService(UserRepository userRepository) {
        logger.info(" ");
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info(" ");
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new AppUserDetails(user);
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
