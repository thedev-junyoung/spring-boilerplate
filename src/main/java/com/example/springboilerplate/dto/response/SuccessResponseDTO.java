package com.example.springboilerplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter, setter, toString, equals, hashCode를 자동으로 생성해주는 어노테이션
@NoArgsConstructor // 기본 생성자를 자동으로 생성해주는 어노테이션
@AllArgsConstructor // 생성자를 자동으로 생성해주는 어노테이션
public class SuccessResponseDTO<T> {
    private String status = "success";
    private int code = 1;
    private String message = "요청이 성공적으로 처리되었습니다.";
    private T data;
}