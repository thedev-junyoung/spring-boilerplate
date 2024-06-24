package com.example.springboilerplate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Comment{

    //@EmbeddedId // 복합 키 사용
    @EmbeddedId
    private CommentId id; // User, Board, UUID 를 합친 복합키

    // @MapId: 복합 키를 사용할 때, 복합 키의 일부를 참조하는 경우 사용
    @MapsId("userId") // CommentId의 userId를 참조. CommentId의 userId와 Comment 의 userId가 같다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("boardId") // CommentId의 boardId를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "parent_user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "parent_board_id", referencedColumnName = "board_id"),
            @JoinColumn(name = "parent_uuid", referencedColumnName = "uuid")
    })
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments;


    @Column(nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

