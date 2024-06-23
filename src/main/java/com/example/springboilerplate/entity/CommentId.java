package com.example.springboilerplate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode // equals()와 hashCode()를 자동으로 생성
@Embeddable
public class CommentId implements Serializable {

    private Long userId;
    private Long boardId;
    @Column(name = "timestamp") // 컬럼 이름이 데이터베이스의 필드와 일치해야 함
    private Instant timestamp;

    public CommentId() {}

    public CommentId(Long userId, Long boardId, Instant timestamp) {
        this.userId = userId;
        this.boardId = boardId;
        this.timestamp = timestamp;
    }
}
