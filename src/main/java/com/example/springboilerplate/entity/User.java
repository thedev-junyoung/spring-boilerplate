package com.example.springboilerplate.entity;

import com.example.springboilerplate.type.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity //JPA 엔터티를 정의
@Getter
@Setter
public class User{
    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값 자동생성
    // GeneratedValue 어노테이션은 기본 키 값을 자동으로 생성하는 방법을 지정
    // - GenerationType.IDENTITY: 기본 키 생성을 데이터베이스에 위임
    // - GenerationType.SEQUENCE: 데이터베이스 시퀀스를 사용하여 기본 키 생성
    // - GenerationType.TABLE: 키 생성 전용 테이블을 사용하여 기본 키 생성
    // - GenerationType.AUTO: 데이터베이스 벤더에 의존하여 기본 키 생성
    // - GenerationType.UUID: UUID를 사용하여 기본 키 생성
    @Column(name = "user_id", nullable = false) // 컬럼명 지정
    private Long userId;


    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 1:N 관계, mappedBy 속성으로 연관관계의 주인을 지정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
