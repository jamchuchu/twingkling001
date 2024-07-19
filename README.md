# 💎예약 구매 프로젝트💎

**Twingkling은 다수의 판매자와 구매자가 동시에 이용할 수 있는 eCommerce 플랫폼입니다.   
사용자들에게 자유로운 상품 거래 환경을 제공하면서,  
대규모 트래픽과 복잡한 거래 상황을 효과적으로 관리할 수 있도록 설계되었습니다.**

### 주요 특징

1. 대규모 트래픽 관리
    - JMeter를 활용한 철저한 부하 테스트로 플랫폼의 안정성 검증
    - 특정 시간 판매 이벤트 등 급격한 트래픽 증가 상황에서도 원활한 서비스 제공
2. 효율적인 주문 상태 관리
    - Redis를 활용한 고속 데이터 처리
    - 스케줄러 기반의 정시 일괄 처리로 시스템 부하 분산 및 성능 최적화
3. 주문 프로세스 최적화
    - 주문 프로세스의 비동기 처리로 응답 속도 향상
    - 락(Lock) 메커니즘 구현으로 동시 접근으로 인한 충돌 방지

</br>

## 프로젝트 실행시간
```
2024년 6월 29일 - 2024년 7월 20일 (4주)
```

## ERD
</br></br>

## API 명세서
</br></br>

## 기술스택
 **Tech Stack**                                                                                                   

- Spring Boot
- Spring JPA

**DB**                                                                                                                                                                                               
- AWS RDS(Mysql)
- Redis

**CI/CD & DevOps**                                                                                                                                                                               
- AWS EC2
- Docker & DokcerHub
- Github Action

**Test**
- Junit5
- Jmeter
  
</br></br>

## 파일구조도

### 전체 구조

```
📦twingkling001
 ┣ 📂address
 ┣ 📂api
 ┣ 📂cart
 ┣ 📂category
 ┣ 📂config
 ┣ 📂product
 ┣ 📂redis
 ┗ 📜Twingkling001Application.java

```

### 모듈별 상세 구조

각 기능 모듈은 다음과 같은 구조를 따릅니다:

```
📂product
 ┣ 📂constant
 ┃ ┣ 📜DetailType.java
 ┃ ┗ 📜SaleState.java
 ┣ 📂controller
 ┃ ┗ 📜ProductController.java
 ┣ 📂dto
 ┃ ┣ 📂request
 ┃ ┃ ┣ 📜ProductDetailReqDto.java
 ┃ ┃ ┗ 📜ProductReqDto.java
 ┃ ┗ 📂response
 ┃ ┃ ┣ 📜ProductDetailRespDto.java
 ┃ ┃ ┗ 📜ProductRespDto.java
 ┣ 📂entity
 ┃ ┣ 📜Product.java
 ┃ ┗ 📜ProductDetail.java
 ┣ 📂repository
 ┃ ┣ 📜ProductDetailRepository.java
 ┃ ┗ 📜ProductRepository.java
 ┗ 📂service
 ┃ ┗ 📜ProductService.java

```

### 주요 특징

1. **모듈화**: 각 기능(address, cart, category 등)이 독립적인 모듈로 구성되어 있습니다.
2. **계층 분리**: 각 모듈 내에서 controller, service, repository 등의 계층이 명확히 분리되어 있습니다.
3. **DTO 패턴**: request와 response DTO를 분리하여 데이터 전송 객체를 효과적으로 관리합니다.
4. **상수 관리**: constant 패키지를 통해 열거형 상수들을 관리합니다.
5. **엔티티 분리**: 데이터베이스 엔티티들이 entity 패키지에 명확히 정의되어 있습니다.

### 장점

- **유지보수성**: 각 기능이 모듈화되어 있어 유지보수가 용이합니다.
- **확장성**: 새로운 기능 추가 시 기존 구조를 따라 쉽게 확장할 수 있습니다.
- **가독성**: 일관된 구조로 인해 코드 탐색과 이해가 쉽습니다.
- **협업**: 개발자 간 작업 영역을 명확히 구분할 수 있어 협업에 유리합니다.

</br></br>

## 주요 기능

### 1. API 응답 표준화 (ApiResponse)
#### 1. 일관된 응답 구조

- `code`: HTTP 상태 코드
- `message`: 응답 메시지
- `data`: 실제 응답 데이터 (선택적)

#### 2. 제네릭 타입 지원

- `<T>`를 사용하여 다양한 타입의 데이터 처리 가능

#### 3. 성공/에러 응답 구분

- `success()` 메소드: 성공 응답 생성
- `error()` 메소드: 에러 응답 생성

#### 4. 응답 타입 표준화

- `SuccessType`과 `ErrorType` 열거형 사용

### 기술적 특징

#### 1. JSON 처리 최적화

```java
@JsonPropertyOrder({"code", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)

```

- 응답 필드 순서 지정
- null이 아닌 데이터만 포함

#### 2. 불변성과 안전성

```java
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

```

- 생성자를 private으로 제한하여 객체 생성 제어

#### 3. 유연한 응답 생성

```java
public static ApiResponse<?> success(SuccessType successType) { ... }
public static <T> ApiResponse<T> success(SuccessType successType, T data) { ... }
public static ApiResponse<?> error(ErrorType errorType) { ... }
public static <T> ApiResponse<T> error(ErrorType errorType, T data) { ... }

```

- 데이터 포함 여부에 따른 다양한 정적 팩토리 메소드 제공

#### 장점

1. 프론트엔드와의 일관된 인터페이스 제공
2. 에러 처리 및 디버깅 용이성 향상
3. 코드 재사용성 증가
4. API 문서화 간소화
