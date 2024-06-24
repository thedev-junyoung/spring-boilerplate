package com.example.springboilerplate.controller;

import com.example.springboilerplate.dto.comment.CommentDTO;
import com.example.springboilerplate.dto.comment.UpdateCommentDTO;
import com.example.springboilerplate.service.CommentService;
import com.example.springboilerplate.utils.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final ResponseFactory responseFactory;

    public CommentController(CommentService commentService, ResponseFactory responseFactory) {
        this.commentService = commentService;
        this.responseFactory = responseFactory;
    }

    @GetMapping // 게시물에 달린 모든 댓글 조회
    public ResponseEntity<?> getAllCommentsByBoardId(@PathVariable Long boardId) {
        logger.info("모든 댓글 가져오기 board ID: {}", boardId);
        List<CommentDTO> comments = commentService.getAllCommentsByBoardId(boardId);
        logger.info("모든 댓글 가져오기 board ID: {}", comments);
        return responseFactory.createSuccessResponse(comments, "모든 댓글 조회 성공");
    }

    @PostMapping // 댓글 생성
    public ResponseEntity<?> createComment(@PathVariable Long boardId, @RequestBody CommentDTO commentDTO) {
        logger.info("Creating comment on board ID: {} with content: {}", boardId, commentDTO.getContent());
        CommentDTO createdComment = commentService.createComment(boardId, commentDTO);
        return responseFactory.createSuccessResponse(createdComment, "댓글 생성 성공");
    }
    @PutMapping("/{commentUuid}")
    public ResponseEntity<?> updateComment(@PathVariable("boardId") Long boardId, @PathVariable UUID commentUuid, @RequestBody UpdateCommentDTO updateCommentDTO) {
        logger.info("Updating comment with UUID: {}", commentUuid);
        CommentDTO updatedComment = commentService.updateComment(commentUuid, updateCommentDTO);
        return responseFactory.createSuccessResponse(updatedComment, "댓글 수정 성공");
    }

    @DeleteMapping("/{commentUuid}")
    public ResponseEntity<?> deleteComment(@PathVariable("boardId") Long boardId, @PathVariable UUID commentUuid) {
        logger.info("Deleting comment with UUID: {}", commentUuid);
        commentService.deleteComment(commentUuid);
        return responseFactory.createSuccessResponse(null, "댓글 삭제 성공");
    }
}
