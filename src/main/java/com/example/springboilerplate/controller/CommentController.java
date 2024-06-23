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

    @PutMapping("/{commentId}") // 댓글 수정
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentDTO updateCommentDTO) {
        logger.info("Updating comment with ID: {}", commentId);
        CommentDTO updatedComment = commentService.updateComment(commentId, updateCommentDTO);
        return responseFactory.createSuccessResponse(updatedComment, "댓글 수정 성공");
    }

    @DeleteMapping("/{commentId}") // 댓글 삭제
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        logger.info("Deleting comment with ID: {}", commentId);
        commentService.deleteComment(commentId);
        return responseFactory.createSuccessResponse(null, "댓글 삭제 성공");
    }
}
