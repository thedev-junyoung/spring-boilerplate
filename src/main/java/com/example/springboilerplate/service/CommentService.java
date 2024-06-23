package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.comment.CommentDTO;
import com.example.springboilerplate.dto.comment.UpdateCommentDTO;
import com.example.springboilerplate.entity.Board;
import com.example.springboilerplate.entity.Comment;
import com.example.springboilerplate.entity.CommentId;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.exception.CustomException;
import com.example.springboilerplate.repository.CommentRepository;
import com.example.springboilerplate.repository.BoardRepository;
import com.example.springboilerplate.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsByBoardId(Long boardId) {
        logger.info("Fetching comments for board ID: {}", boardId);
        List<Comment> comments = commentRepository.findByBoardBoardId(boardId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }
    @Transactional
    public CommentDTO createComment(Long boardId, CommentDTO commentDTO) {
        logger.info("Creating comment on board ID: {}", boardId);
        Board board = findBoardById(boardId);
        User user = findUserById(commentDTO.getUserId());

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());

        Comment createdComment = commentRepository.save(comment);
        return modelMapper.map(createdComment, CommentDTO.class);
    }


    @Transactional
    public CommentDTO updateComment(Long id, UpdateCommentDTO updateCommentDTO) {
        logger.info("Updating comment with ID: {}", id);
        Comment comment = findCommentById(id);
        comment.setContent(updateCommentDTO.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    @Transactional
    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        Comment comment = findCommentById(id);
        commentRepository.delete(comment);
    }

    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    Map<String, Object> errorDetails = new HashMap<>();
                    errorDetails.put("boardId", boardId);
                    errorDetails.put("error", "Board not found");
                    return new CustomException("Board not found with id: " + boardId, HttpStatus.NOT_FOUND, errorDetails);
                });
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    Map<String, Object> errorDetails = new HashMap<>();
                    errorDetails.put("userId", userId);
                    errorDetails.put("error", "User not found");
                    return new CustomException("User not found with id: " + userId, HttpStatus.NOT_FOUND, errorDetails);
                });
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    Map<String, Object> errorDetails = new HashMap<>();
                    errorDetails.put("commentId", id);
                    errorDetails.put("error", "Comment not found");
                    return new CustomException("Comment not found with id: " + id, HttpStatus.NOT_FOUND, errorDetails);
                });
    }

}
