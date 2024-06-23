package com.example.springboilerplate.repository;

import com.example.springboilerplate.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByBoardBoardId(Long boardId); // 올바른 메소드 정의
}
