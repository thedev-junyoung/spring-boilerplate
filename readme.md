## 프로젝트 구조
```bash
├── java
│   └── com
│       └── example
│           └── springboilerplate
│               ├── SpringBoilerplateApplication.java
│               ├── config
│               │   ├── AppConfig.java
│               │   ├── ModelMapperConfig.java
│               │   └── SecurityConfig.java
│               ├── controller
│               │   ├── BoardController.java
│               │   ├── CommentController.java
│               │   ├── MainController.java
│               │   └── UserController.java
│               ├── dto
│               │   ├── board
│               │   ├── comment
│               │   ├── response
│               │   └── user
│               ├── entity
│               │   ├── Board.java
│               │   ├── Comment.java
│               │   ├── CommentId.java
│               │   └── User.java
│               ├── exception
│               │   ├── CustomException.java
│               │   └── CustomExceptionHandler.java
│               ├── filters
│               │   ├── JWTFilter.java
│               │   ├── LoginFilter.java
│               │   └── NoOpFilter.java
│               ├── repository
│               │   ├── BoardRepository.java
│               │   ├── CommentRepository.java
│               │   └── UserRepository.java
│               ├── service
│               │   ├── BoardService.java
│               │   ├── CommentService.java
│               │   ├── JoinService.java
│               │   ├── LoginService.java
│               │   └── UserService.java
│               ├── type
│               │   └── UserRole.java
│               └── utils
│                   ├── JWT.java
│                   ├── JsonResponseFactory.java
│                   └── ResponseFactory.java
└── resources
├── application.properties
├── logback-spring.xml
├── static
└── templates
```
## 주요내용
### 보안 및 인증
이 애플리케이션은 JWT 인증 방식을 사용함. 
모든 요청은 `JWTFilter`를 통해 인증된 사용자에게만 접근을 허용.

JWT 인증: 로그인 시 JWT 토큰을 발급받고, 이후 모든 API 요청 시 `Authorization` 헤더에 `JWT` 토큰을 포함시켜야 함

### 응답 추상화
응답을 일관되게 관리하기 위해 ResponseFactory와 JsonResponseFactory를 사용하여 API 응답을 추상화 함. 
성공 및 오류 응답을 표준화하고, 클라이언트가 예측 가능한 방식으로 응답을 처리 가능하도록 구현

### 예외 처리
전역 예외 처리기를 사용하여 다양한 예외 상황에 대해 일관된 오류 응답을 제공
`CustomExceptionHandler` 클래스를 통해 발생 가능한 다양한 예외를 처리하고, `ErrorResponseDTO` 형식으로 클라이언트에게 반환

### EmbeddedId 복합키 설정
`Comment` 엔터티에서 `CommentId`를 `EmbeddedId`로 사용하여 복합 키를 구성. 
`Comment`는 `userId`, `boardId`, `uuid`를 포함한 복합 키

### N+1 문제 해결
`Fetch Join`: JPQL을 사용하여 Fetch Join을 설정하여 연관된 엔터티를 한 번의 쿼리로 가져옵니다.
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments WHERE b.boardId = :boardId")
    Optional<Board> findByIdWithComments(Long boardId);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments")
    List<Board> findAllWithComments();
}
```

