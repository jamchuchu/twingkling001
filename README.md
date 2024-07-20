# 💎예약 구매 프로젝트💎

**Twingkling은 다수의 판매자와 구매자가 동시에 이용할 수 있는 eCommerce 플랫폼입니다  
사용자들에게 자유로운 상품 거래 환경을 제공하면서,  
대규모 트래픽과 복잡한 거래 상황을 효과적으로 관리할 수 있도록 설계 하였습니다**

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

## 📅 프로젝트 실행시간
```
2024년 6월 29일 - 2024년 7월 20일 (4주)
```


## 📊 ERD
https://www.erdcloud.com/d/BatZefBiFtRkQefui
![image](https://github.com/user-attachments/assets/d720ff2d-1a2d-4ff4-99d4-89b2face7ceb)
### 📘ERD 설계 상세 설명

<details>
<summary>펼치기/접기</summary>

### 사용자 관리 시스템
1. **Member**
   - 목적: 로그인에 필요한 기본 정보만 포함
   - 특징: 보안과 성능을 고려하여 핵심 로그인 정보만 저장

2. **Member Detail**
   - 목적: 로그인 외 개인정보 저장
   - 이점: 데이터 분리로 보안 강화 및 성능 최적화

3. **Member Address**
   - 구조: 메인 주소(고정)와 서브 주소(최근 5개)로 분리
   - 관리 방식: 최근 사용 기준으로 서브 주소 자동 관리

4. **Role**
   - 목적: 사용자 역할 관리 (판매자/구매자)
   - 특징: 독립적인 ID 부여로 유연한 역할 관리

5. **Address Detail**
   - 목적: 주소 정보의 일관성 유지
   - 이점: 중복 데이터 최소화 및 주소 형식 표준화

### 상품 및 주문 시스템
1. **Product**
   - 내용: 판매 상품의 상세 정보 포함

2. **Category**
   - 구조: 트리 형태의 계층적 구조 (upper category 활용)
   - 특징: 유연한 카테고리 depth 관리 가능

3. **Order**
   - 구조: 주문 건당 1개 레코드 생성
   - 포함 정보: 배송비, 전체 가격 등 주문 수준의 정보

4. **Order Detail**
   - 목적: 주문 내 개별 상품 정보 관리
   - 이점: 주문의 세부 내역 추적 용이

5. **Pay**
   - 내용: 결제 방식, 소셜 결제 정보 등 포함

### 부가 기능
1. **Review**
   - 목적: 상품 리뷰 관리

2. **Like**
   - 목적: 상품 선호도 표시 기능


</details>

### 💡ERD 작성 시 고민 사항

<details>
<summary>펼치기/접기</summary>

### 기획 단계에서의 주요 고려사항

1. **주문 시스템**
   - 비회원 주문 허용 여부
   - 다중 판매자 vs 단일 판매자 시스템

2. **주소 시스템**
   - 도로명 주소 입력 방식 (API 활용) 및 응답 형식
   - 주소 저장 및 관리 방식
     * 메인 주소 고정 (PRIMARY)
     * 최근 사용 기준 5개 주소 유지
     * 새 주문 시 이전 주소 탐색 및 저장 로직
     * 주소 삭제 관리 (DELETED_YN 필드 활용 및 스케줄링 삭제)

3. **카테고리 시스템**
   - 카테고리 깊이(depth) 설정
   - Upper ID 구현 또는 트리 방식 채택 여부

4. **사용자 관리**
   - 판매자와 구매자 ID 별도 관리 vs 동시 허용

5. **장바구니(카트) 시스템**
   - 상품 판매 종료 시 카트 내 해당 상품 처리 방식
     * 트리거 사용?
     * 주기적 카트 검사 및 삭제?

### ERD 설계 시 중점 사항

1. **확장성**: 향후 기능 추가 및 시스템 확장을 고려한 설계
2. **성능**: 대용량 데이터 처리 및 빠른 쿼리 실행을 위한 구조
3. **일관성**: 데이터 무결성 유지를 위한 관계 설정
4. **유연성**: 다양한 비즈니스 요구사항 변화에 대응 가능한 구조
5. **보안**: 사용자 데이터 보호 및 접근 제어를 고려한 설계

</details>
</br></br>

## 📡API 명세서
https://www.notion.so/API-7dc60dd21e784f9e8ddf85740db74dc4
</br></br>

## 🧩아키텍처
![image](https://github.com/user-attachments/assets/1a6889ce-ccf0-4594-9cc5-3a9f26f166a9)

</br></br>


## 🛠 기술스택
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

## 📂 파일구조도

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

### 특징
<details>
<summary>펼치기/접기</summary>  

- **모듈화**: 각 기능(address, cart, category 등)이 독립적인 모듈로 구성되어 있습니다.
- **계층 분리**: 각 모듈 내에서 controller, service, repository 등의 계층이 명확히 분리되어 있습니다.
- **DTO 패턴**: request와 response DTO를 분리하여 데이터 전송 객체를 효과적으로 관리합니다.
- **상수 관리**: constant 패키지를 통해 열거형 상수들을 관리합니다.
- **엔티티 분리**: 데이터베이스 엔티티들이 entity 패키지에 명확히 정의되어 있습니다.
</details>



### 장점

<details>
<summary>펼치기/접기</summary>

- **유지보수성**: 각 기능이 모듈화되어 있어 유지보수가 용이합니다.
- **확장성**: 새로운 기능 추가 시 기존 구조를 따라 쉽게 확장할 수 있습니다.
- **가독성**: 일관된 구조로 인해 코드 탐색과 이해가 쉽습니다.
- **협업**: 개발자 간 작업 영역을 명확히 구분할 수 있어 협업에 유리합니다.

</details>

</br></br>

# 🔑 주요 기능
## 📊 API 응답 표준화 (ApiResponse)
일관된 형식의 API 응답을 제공하여 클라이언트와의 효율적인 통신을 보장합니다.
<details>
<summary>펼치기/접기</summary>

### 1. JSON 처리 최적화
```java
@JsonPropertyOrder({"code", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
```
- 응답 필드 순서 지정
- null이 아닌 데이터만 포함

### 2. 불변성과 안전성
```java
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
```
- 생성자를 private으로 제한하여 객체 생성 제어

### 3. 유연한 응답 생성
```java
public static ApiResponse<?> success(SuccessType successType) { ... }
public static <T> ApiResponse<T> success(SuccessType successType, T data) { ... }
public static ApiResponse<?> error(ErrorType errorType) { ... }
public static <T> ApiResponse<T> error(ErrorType errorType, T data) { ... }
```
- 데이터 포함 여부에 따른 다양한 정적 팩토리 메소드 제공

### 장점
- 프론트엔드와의 일관된 인터페이스 제공
- 에러 처리 및 디버깅 용이성 향상
- 코드 재사용성 증가
- API 문서화 간소화

</details>

</br></br>


## 🌳계층형 상품 카테고리 시스템
상위 카테고리에서 하위 카테고리로 세분화되는 구조를 통해 정확한 상품 분류가 가능합니다.
<details>
<summary>펼치기/접기</summary>

| 칼럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| category_id | BIGINT | PK |
| upper_category_id | BIGINT | 상위 카테고리 ID |
| category_name | VARCHAR(255) | 카테고리 이름 |

### 주요 기능

#### 1. 하위 카테고리 조회

- 엔드포인트: `GET /{categoryId}/lower`
- 기능: 특정 카테고리의 모든 하위 카테고리 조회
- 사용 예: '바지' 카테고리에서 '청바지', '반바지' 등의 하위 카테고리 조회

#### 2. 상위 카테고리 조회

- 엔드포인트: `GET /{categoryId}/upper`
- 기능: 특정 카테고리의 모든 상위 카테고리 순서대로 조회
- 사용 예: '청반바지'에서 '반바지' → '바지' 순으로 상위 카테고리 조회

#### 3. 카테고리 및 하위 카테고리 삭제

- 엔드포인트: `DELETE /{categoryId}/lower`
- 기능: 특정 카테고리와 그에 속한 모든 하위 카테고리 삭제
- 사용 예: '반바지' 삭제 시 '청반바지', '백반바지' 등 모든 하위 카테고리도 함께 삭제

### 장점

- 상세한 상품 분류 가능
- 직관적인 카테고리 관리
- 효율적인 카테고리 검색 및 탐색
- 유연한 카테고리 구조 변경 가능

### 구현 시 고려사항

- 재귀적 쿼리를 통한 효율적인 계층 구조 탐색
- 카테고리 삭제 시 데이터 정합성 유지
- 대량의 카테고리 데이터 처리 시 성능 최적화

### 기대 효과

- 복잡한 상품 구조를 효과적으로 관리 
- 판매자들에게 정확한 상품 분류 옵션제공
- 계층적 구조를 통해 상품 검색과 관리가 용이
- e-commerce 플랫폼의 확장 용이
  
</details>
</br></br>


## ✉️ 이메일 인증을 통한 회원가입 프로세스
![image](https://github.com/user-attachments/assets/5556bb4f-054d-4431-90be-f0c1c2e1f249)


<details>
<summary>펼치기/접기</summary>

## 프로세스 흐름
1. 사용자 회원가입 요청
    - 사용자가 로그인 정보를 입력하여 서버에 전송
2. 서버에서 데이터 처리
    - 로그인 정보를 암호화 후 Redis에 임시 저장
    - MySQL에 로그인 정보 저장
3. 이메일 인증 요청
    - 서버가 메일 서버에 인증 메일 발송 요청
    - 메일 서버가 사용자에게 인증 링크가 포함된 이메일 발송
4. 사용자 이메일 인증
    - 사용자가 이메일의 인증 링크 클릭
    - 서버로 인증 요청 전송
5. 인증 완료 및 토큰 발급
    - 서버가 인증을 확인하고 사용자에게 토큰 발급
</details>
</br></br>


## 📦주문 배송 스케줄러
주문된 상품의 배송 상태를 자동으로 업데이트하고 관리하는 시스템입니다.

<details>
<summary>펼치기/접기</summary>

## 주요 기능
### 1. 배송 중 → 배송 완료 상태 변경
- 기능:
    - Redis에서 배송 중인 주문 목록을 조회
    - 각 주문의 상태를 '배송 완료'로 업데이트
    - 배송 완료 시간을 현재 시간으로 설정
    - 처리 완료된 주문을 Redis에서 삭제
### 2. 결제 완료 → 배송 중 상태 변경
- 기능:
    - Redis에서 배송 대기 중인 주문 목록을 조회
    - 각 주문의 상태를 '배송 중'으로 업데이트
    - 업데이트된 주문을 '배송 중' 목록에 추가
    - 처리 완료된 주문을 배송 대기 목록에서 삭제

</details>
</br></br>

## 🧪 단위 테스트 (JUnit)

<details>
<summary>펼치기/접기</summary>

- JUnit 5 사용
- Mockito와 통합 (`@ExtendWith(MockitoExtension.class)`)

## 2. 테스트 구조
- Given-When-Then 패턴 적용
- `@Mock`과 `@InjectMocks`를 활용한 의존성 주입

## 3. 장점
1. 유지보수성: 모듈화된 구조로 유지보수 용이
2. 확장성: 일관된 구조로 새로운 기능 추가 시 쉬운 확장
3. 가독성: 표준화된 구조로 코드 탐색과 이해 용이
4. 협업 효율: 개발자 간 작업 영역 명확히 구분 가능
5. 코드 품질 향상: 단위 테스트를 통한 버그 조기 발견 및 수정
6. 리팩토링 안정성: 테스트 기반으로 안전한 코드 변경 가능
7. 문서화 효과: 테스트 코드가 실제 코드의 동작을 설명하는 역할
8. 개발 생산성 향상: 장기적으로 버그 수정 시간 감소 및 새 기능 개발 속도 향상

</details>
</br></br>

# 🔧 트러블 슈팅
## ⚡ 주문 시스템 성능 개선

<details>
<summary>펼치기/접기</summary>

## 주요 문제점
1. 대량 주문 시 처리 속도 저하 (0.041 Apdex 스코어)
2. 5000건 주문 시 일부 재고만 차감되는 문제 (500건만 처리됨)
3. 동시 주문에 대한 재고 처리 불일치

## 해결 과정
1. Redis를 활용한 캐싱 시도
2. 비동기 처리 도입
3. 데이터베이스와 Redis 동시 업데이트 전략
4. 행 락(Row Lock) 적용

## 최종 채택 솔루션
비동기 처리를 통한 DB 저장 및 Redis 입력 동시 수행, 최대 수준의 행 락 적용

## 성능 테스트 결과
| 테스트 조건 | 테스트 수 | Apdex 스코어 | 비고 |
| --- | --- | --- | --- |
| 10개 단건 주문 | 10 | 1.000 |  |
|  다량 주문 | 1000*5 | 0.041 | 5000건 중 500건 삭제 |
| 다량 주문 (Redis 활용) | 500*5 | 0.095 | 2500건 중 약 1200건 삭제 |
| DB/Redis 비동기 처리 | 500*5 | 0.584 | 3000건 중 재고 약 1200건 삭제 |
| Redis 미사용, 비동기 처리 | 1000*5 | 0.714 | 5000r건 중 재고 5000건 삭제 |

## 결론
최종적으로 Redis를 사용하지 않고 비동기 처리만을 적용한 방식은 
Apdex 스코어 0.714를 기록하며, 대량 주문 처리 시 안정적인 성능을 보여주었습니다.

</details>
</br></br>

