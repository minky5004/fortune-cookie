# Fortune Cookie - 하루 한 번 포춘쿠키

포춘쿠키를 클릭해서 오늘의 운세를 확인하는 웹 앱입니다.

## 🎯 주요 기능

- ✅ **포춘쿠키 파괴** - 쿠키를 5번 클릭하면 파괴 애니메이션 후 오늘의 운세 공개
- ✅ **하루 1회 제한** - LocalStorage 기반 날짜 체크로 하루에 한 번만 운세 확인 가능
- ✅ **12개 카테고리** - 행운, 사랑, 직업, 건강, 재물, 학업 등 카테고리별 운세 200개
- ✅ **REST API** - 운세 랜덤 조회 JSON API 제공
- ✅ **예외 처리** - GlobalExceptionHandler를 통한 일관된 에러 응답
- ✅ **DB 마이그레이션** - Flyway로 스키마 변경 이력 관리

## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.0.6 |
| **View** | Thymeleaf + 순수 JS (프레임워크 없음) |
| **Database** | PostgreSQL 16 |
| **Migration** | Flyway |
| **Testing** | JUnit 5 + Mockito |
| **Build** | Gradle |
| **Infra** | Docker Compose |

## ✅ 프로젝트 완성도

| 항목 | 진행도 | 설명 |
|------|--------|------|
| 기능 구현 | 100% | API + 프론트엔드 + 파괴 메커니즘 완성 |
| 단위 테스트 | 100% | 5개 테스트 (모두 통과) |
| DB 마이그레이션 | 100% | Flyway V1, V2 적용 완료 |
| 문서화 | 100% | README 작성 완료 |

### 테스트

```
✅ FortuneServiceTest   (2개) - 랜덤 운세 반환, 데이터 없을 때 예외
✅ FortuneControllerTest (3개) - 메인 페이지, 운세 API JSON 응답, 500 에러 응답

총 5개 테스트 → BUILD SUCCESSFUL ✅
```

## 📦 설치 방법

### 사전 요구사항

- Docker & Docker Compose
- Java 21

### 1. 저장소 클론

```bash
git clone https://github.com/minky5004/fortune-cookie.git
cd fortune-cookie
```

### 2. 환경변수 설정

`.env.example`을 참고해 `application-local.properties` 파일 생성:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fortunecookie
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3-A. Docker Compose로 DB 실행 후 앱 시작 (권장)

```bash
# PostgreSQL 컨테이너 기동
docker compose up -d

# 앱 실행 (local 프로파일 자동 적용)
./gradlew bootRun
```

### 3-B. 환경변수로 DB 비밀번호 지정

```bash
DB_PASSWORD=mypassword docker compose up -d
./gradlew bootRun
```

## 🚀 실행 방법

### 앱 접속

```
http://localhost:8080
```

### 헬스 체크

```bash
curl http://localhost:8080/api/fortune
```

### 로그 확인

```bash
docker compose logs -f
```

## 📡 API 사용 예시

### 운세 조회

**GET** `/api/fortune`

```bash
curl http://localhost:8080/api/fortune
```

응답:

```json
{
  "id": 42,
  "message": "오늘은 새로운 시작을 위한 완벽한 날입니다.",
  "category": "행운"
}
```

### 에러 응답

운세 데이터가 없는 경우:

```json
{
  "status": 500,
  "message": "운세 데이터가 없습니다."
}
```

## ⚙️ 환경 설정

### Spring Profile

| 프로파일 | 용도 | DB |
|---------|------|----|
| `local` | 로컬 Docker 개발 | `localhost:5432` |
| `prod` | 프로덕션 | 환경변수 필수 |

기본 프로파일은 `local`. `SPRING_PROFILES_ACTIVE` 환경변수로 오버라이드.

### application-local.properties 예시

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fortunecookie
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## 📊 데이터베이스 스키마

### fortune 테이블

```sql
CREATE TABLE IF NOT EXISTS fortune (
    id       BIGSERIAL    PRIMARY KEY,
    message  VARCHAR(500) NOT NULL,
    category VARCHAR(255) NOT NULL
);
```

### Flyway 마이그레이션 이력

| 버전 | 설명 |
|------|------|
| V1 | fortune 테이블 초기 생성 |
| V2 | category 컬럼 추가 |

## 🎓 프로젝트 하이라이트

### 아키텍처 특징

- **Layered Architecture**: Controller → Service → Repository 패턴
- **DTO 분리**: `FortuneResponse(id, message, category)`로 엔티티 직접 노출 방지
- **스키마 버전 관리**: Flyway + `ddl-auto=validate`로 안전한 마이그레이션
- **데이터 초기화**: `DataInitializer`가 기동 시 DB가 비어 있을 때만 운세 200개 삽입

### 프론트엔드 특징

- **IIFE 단일 모듈**: `main.js` 전체가 즉시 실행 함수 하나로 구성
- **5번 클릭 파괴**: 히트 카운트 누적 → 파괴 애니메이션 → API 호출 순서
- **하루 1회 제한**: `fortuneCookieDate`, `fortuneCookieMessage` 키를 LocalStorage에 저장

### 학습 포인트

이 프로젝트는 다음을 학습하기에 좋은 예제입니다:

- Spring Boot 4 기반 REST API 설계
- Flyway를 통한 DB 스키마 버전 관리
- Spring Profile로 환경별 설정 분리
- MockMvc standaloneSetup 기반 컨트롤러 단위 테스트 (`@WebMvcTest` 미사용)
- LocalStorage를 활용한 클라이언트 측 상태 관리

## 📝 라이선스

MIT License

## 📧 연락처

- Email: minky5004@gmail.com
- GitHub: [@minky5004](https://github.com/minky5004)
