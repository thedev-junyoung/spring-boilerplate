package com.example.springboilerplate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode // equals()와 hashCode()를 자동으로 생성
@Embeddable
public class CommentId implements Serializable {

    private Long userId;
    private Long boardId;
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    public CommentId() {}

    public CommentId(Long userId, Long boardId, UUID uuid) {
        this.userId = userId;
        this.boardId = boardId;
        this.uuid = uuid;
    }
}
