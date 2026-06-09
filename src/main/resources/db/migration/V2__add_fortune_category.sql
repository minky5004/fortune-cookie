-- 기존 데이터 초기화 (카테고리 없는 구 데이터 제거, DataInitializer가 재삽입)
TRUNCATE TABLE fortune;

-- category 컬럼 추가 (빈 테이블이므로 NOT NULL 바로 적용 가능)
ALTER TABLE fortune
    ADD COLUMN IF NOT EXISTS category VARCHAR(255) NOT NULL DEFAULT 'LUCK';

-- 기본값 제거 (이후 INSERT는 명시적으로 category를 제공해야 함)
ALTER TABLE fortune
    ALTER COLUMN category DROP DEFAULT;
