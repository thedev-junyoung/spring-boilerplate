package com.example.springboilerplate.repository;

import com.example.springboilerplate.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByBoardBoardId(Long boardId); // Board ID로 댓글 조회

    Optional<Comment> findByIdUuid(UUID uuid); // UUID 로 댓글 조회
}
