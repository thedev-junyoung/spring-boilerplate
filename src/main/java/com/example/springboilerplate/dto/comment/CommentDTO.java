package com.example.springboilerplate.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long commentId; // 댓글 ID
    private Long userId; // 댓글 작성자 ID
    private Long boardId; // 댓글이 달린 게시물 ID
    private String content; // 댓글 내용
    private LocalDateTime createdAt; // 댓글 작성 시간
    private LocalDateTime updatedAt;

}
