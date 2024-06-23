package com.example.springboilerplate.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
