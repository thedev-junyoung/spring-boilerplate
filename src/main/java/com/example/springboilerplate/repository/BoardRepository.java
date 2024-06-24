package com.example.springboilerplate.repository;

import com.example.springboilerplate.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 게시판 조회 시 댓글 목록을 함께 조회, N+1 문제 해결
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments WHERE b.boardId = :boardId")
    Optional<Board> findByIdWithComments(Long boardId);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments")
    List<Board> findAllWithComments();
}
