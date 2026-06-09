# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

Spring Boot 4 기반 포춘쿠키 웹앱. 하루 1회 랜덤 운세를 제공한다.

- **런타임:** Java 21, Spring Boot 4.0.6, Gradle
- **DB:** PostgreSQL 16 (Docker), 스키마 관리는 Flyway
- **뷰:** Thymeleaf 단일 페이지, 순수 JS (프레임워크 없음)

## 개발 환경 실행

```bash
# 1. PostgreSQL 컨테이너 기동
docker compose up -d

# 2. 앱 실행 (local 프로파일 자동 적용)
./gradlew bootRun
```

`application-local.properties`는 gitignore 대상이므로 로컬에 직접 생성해야 한다. `.env.example` 참고.

## 주요 명령어

```bash
# 전체 테스트
./gradlew test

# 단일 테스트 클래스
./gradlew test --tests "com.example.fortunecookie.service.FortuneServiceTest"

# 단일 테스트 메서드
./gradlew test --tests "com.example.fortunecookie.service.FortuneServiceTest.랜덤_운세를_반환한다"

# 빌드
./gradlew build
```

## 아키텍처

### 요청 흐름

```
브라우저 → FortuneController → FortuneService → FortuneRepository (PostgreSQL)
                ↓
        FortuneResponse DTO 반환
```

- `GET /` → Thymeleaf `index.html` 반환
- `GET /api/fortune` → `FortuneResponse(id, message, category)` JSON 반환
- 예외는 `GlobalExceptionHandler`가 `ErrorResponse(status, message)` 형식으로 처리

### 운세 데이터

`DataInitializer`가 앱 기동 시 DB가 비어 있으면 운세 200개를 삽입한다. `fortune` 테이블에 데이터가 1건이라도 있으면 실행하지 않는다.

`FortuneRepository.findRandom()`은 `ORDER BY RANDOM() LIMIT 1` 네이티브 쿼리를 사용한다.

### DB 마이그레이션 (Flyway)

스키마 변경은 반드시 `src/main/resources/db/migration/` 아래 `V{n}__설명.sql` 파일로 추가한다. `ddl-auto=validate`이므로 Hibernate는 스키마를 변경하지 않는다.

- `V1` — fortune 테이블 초기 생성
- `V2` — category 컬럼 추가

### 테스트 전략

- **FortuneServiceTest** — Mockito로 repository 모킹, 순수 단위 테스트
- **FortuneControllerTest** — `MockMvcBuilders.standaloneSetup` 사용 (`@WebMvcTest`는 Spring Boot 4에서 제거됨)
- 테스트 환경은 H2 인메모리 DB, Flyway 비활성화 (`spring.flyway.enabled=false`)

### 프론트엔드

`main.js`가 IIFE 하나로 구성된다. LocalStorage(`fortuneCookieDate`, `fortuneCookieMessage`)로 하루 1회 제한을 관리한다. 쿠키 클릭 → 5회 히트 → 파괴 애니메이션 → `GET /api/fortune` 호출 순서로 동작한다.

## Git 컨벤션

- 브랜치: `feat/`, `fix/`, `chore/`, `refactor/` 접두어 사용, `dev` 기반으로 분기
- PR 제목: `[FEAT]`, `[FIX]`, `[CHORE]`, `[REFACTOR]`, `[STYLE]`, `[DOCS]` 대문자 태그
- 라벨: `frontend`, `backend` (RatDance 프로젝트와 동일한 컨벤션)
- 커밋 전 반드시 사용자에게 내용 확인 후 진행
- 커밋 메시지에 `Co-Authored-By` 포함 금지
- `dev → main` 머지는 사용자 승인 후 진행

## Spring Profile

| 프로파일 | 용도 | DB 비밀번호 |
|---------|------|------------|
| `local` | 로컬 Docker 개발 | 기본값 `postgres` |
| `prod`  | 프로덕션 | 환경변수 필수 |

기본값은 `local` (`SPRING_PROFILES_ACTIVE` 환경변수로 오버라이드).
