package com.example.springboilerplate.dto.user;

import com.example.springboilerplate.type.UserRole;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    Long userId;
    String email;
    String username;
    UserRole role;
    String profileImg;
    Date createdAt;
}
