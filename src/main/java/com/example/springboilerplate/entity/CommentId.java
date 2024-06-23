package com.example.springboilerplate.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable // 복합 키를 정의하는 어노테이션
public class CommentId implements Serializable {

    private Long userId; // 사용자 ID
    private Long boardId; // 게시글 ID

    // 기본 생성자 필요
    public CommentId() {}

    public CommentId(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }
}