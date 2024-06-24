package com.example.springboilerplate.dto.comment;

import com.example.springboilerplate.entity.CommentId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentDTO {
    private UUID uuid; // 댓글 ID로 UUID 사용
    private Long userId; // 댓글 작성자 ID
    private Long boardId; // 댓글이 달린 게시물 ID
    private String content; // 댓글 내용
    // ModelMapper를 사용하여 LocalDateTime -> String으로 변환
    // why? LocalDateTime은 JSON으로 변환할 수 없기 때문 (직렬화)
    private String createdAt; // 댓글 작성 시간
    private String updatedAt;

}
