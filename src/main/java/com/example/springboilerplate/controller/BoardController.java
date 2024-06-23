package com.example.springboilerplate.controller;

import com.example.springboilerplate.dto.board.BoardDTO;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.example.springboilerplate.service.BoardService;
import com.example.springboilerplate.utils.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")  // 모든 메소드를 /boards 경로 아래로 이동
public class BoardController {
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;
    private final ResponseFactory responseFactory;

    public BoardController(BoardService boardService, ResponseFactory responseFactory) {
        this.boardService = boardService;
        this.responseFactory = responseFactory;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@PathVariable Long id) {
        BoardDTO boardDTO = boardService.getBoardById(id);
        return responseFactory.createSuccessResponse(boardDTO, "게시판 조회 완료");
    }

    @GetMapping("/")
    public ResponseEntity<SuccessResponseDTO<List<BoardDTO>>> getAllBoards() {
        List<BoardDTO> boards = boardService.getAllBoards();
        return responseFactory.createSuccessResponse(boards, "모든 게시판 조회 완료");
    }

    @PostMapping("/")
    public ResponseEntity<?> createBoard(@RequestBody BoardDTO boardDTO) {
        BoardDTO createdBoard = boardService.createBoard(boardDTO);
        return responseFactory.createSuccessResponse(createdBoard, "게시판 생성 성공");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {
        logger.info("Updating board with id: " + id);
        BoardDTO updatedBoard = boardService.updateBoard(id, boardDTO);
        return responseFactory.createSuccessResponse(updatedBoard, "게시판 업데이트 성공완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return responseFactory.createSuccessResponse(null, "게시판 삭제 완료");
    }
}
