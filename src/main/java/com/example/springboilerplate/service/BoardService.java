package com.example.springboilerplate.service;

import com.example.springboilerplate.dto.board.BoardDTO;
import com.example.springboilerplate.entity.Board;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.exception.CustomException;
import com.example.springboilerplate.repository.BoardRepository;
import com.example.springboilerplate.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    public BoardService(BoardRepository boardRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public BoardDTO getBoardById(Long id) {
        Board board = findBoardById(id);
        return modelMapper.map(board, BoardDTO.class);
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public List<BoardDTO> getAllBoards() {
        logger.info("Retrieving all boards");
        return boardRepository.findAll().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional // 트랜잭션 처리
    public BoardDTO createBoard(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);
        User user = findUserById(boardDTO.getUserId());
        board.setUser(user);
        Board createdBoard = boardRepository.save(board);
        return modelMapper.map(createdBoard, BoardDTO.class);
    }

    @Transactional // 트랜잭션 처리
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        Board board = findBoardById(id);
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());

        Board updatedBoard = boardRepository.save(board);
        return modelMapper.map(updatedBoard, BoardDTO.class);
    }

    @Transactional // 트랜잭션 처리
    public void deleteBoard(Long id) {
        Board board = findBoardById(id);
        boardRepository.delete(board);
    }


    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                //.orElseThrow(() -> new CustomException("User not found with id: " + userId, HttpStatus.NOT_FOUND));
                .orElseThrow(()->{
                    Map<String, Object> errorDetails = new HashMap<>();
                    errorDetails.put("userId", userId);
                    errorDetails.put("error", "User not found");
                    return new CustomException("User not found with id: " + userId, HttpStatus.NOT_FOUND, errorDetails);
                });
    }
    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    Map<String, Object> errorDetails = new HashMap<>();
                    errorDetails.put("userId", boardId);
                    errorDetails.put("error", "User not found");
                    return new CustomException("Board not found with id: " + boardId, HttpStatus.NOT_FOUND,errorDetails);
                });
    }
}
