package com.example.springboilerplate.dto.user;

import com.example.springboilerplate.type.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data // Getter, Setter, toString, equals, hashCode를 자동으로 생성

public class UserDTO {
    Long userId;
    String email;
    String username;
    UserRole role;
    String profileImg;
    LocalDateTime createdAt; // LocalDateTime으로 변경
    // LocalDateTime은 날짜와 시간을 모두 포함하는 클래스
    // Date 클래스는 날짜와 시간을 모두 포함하지만, Date 클래스는 날짜와 시간을 나누어서 사용할 수 없음
    // LocalDateTime 클래스는 날짜와 시간을 나누어서 사용할 수 있음
}
