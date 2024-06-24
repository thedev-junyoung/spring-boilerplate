package com.example.springboilerplate.dto.board;

import com.example.springboilerplate.dto.comment.CommentDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardDTO {
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDTO> comments; // 댓글 목록 추가

}
