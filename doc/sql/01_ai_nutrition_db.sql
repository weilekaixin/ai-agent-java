-- 连接 ai_nutrition_db 后执行
-- CREATE DATABASE ai_nutrition_db;

-- ----------------------------
-- 食物分类表
-- ----------------------------
CREATE TABLE IF NOT EXISTS food_category
(
    id          BIGINT       NOT NULL,
    name        VARCHAR(64)  NOT NULL,
    icon        VARCHAR(255),
    sort        INT          NOT NULL DEFAULT 0,
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ  NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ  NOT NULL DEFAULT now(),
    del_flag    SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_food_category PRIMARY KEY (id)
);

COMMENT ON TABLE food_category IS '食物分类';

-- ----------------------------
-- 食物表（每100g营养数据）
-- ----------------------------
CREATE TABLE IF NOT EXISTS food
(
    id          BIGINT         NOT NULL,
    name        VARCHAR(128)   NOT NULL,
    category_id BIGINT,
    calories    NUMERIC(8, 2)  NOT NULL DEFAULT 0,
    protein     NUMERIC(8, 2)  NOT NULL DEFAULT 0,
    fat         NUMERIC(8, 2)  NOT NULL DEFAULT 0,
    carbs       NUMERIC(8, 2)  NOT NULL DEFAULT 0,
    fiber       NUMERIC(8, 2)  NOT NULL DEFAULT 0,
    unit        VARCHAR(16)    NOT NULL DEFAULT 'g',
    source      VARCHAR(32)    NOT NULL DEFAULT 'manual',
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ    NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ    NOT NULL DEFAULT now(),
    del_flag    SMALLINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_food PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_food_category ON food (category_id) WHERE del_flag = 0;
CREATE INDEX IF NOT EXISTS idx_food_name ON food USING gin (to_tsvector('simple', name));

COMMENT ON TABLE food IS '食物库（每100g营养数据）';
COMMENT ON COLUMN food.unit IS 'g/ml/个';
COMMENT ON COLUMN food.source IS 'manual=手动录入 api=外部API导入';

-- ----------------------------
-- 饮食记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS meal
(
    id          BIGINT        NOT NULL,
    user_id     BIGINT        NOT NULL,
    food_id     BIGINT        NOT NULL,
    amount      NUMERIC(8, 2) NOT NULL,
    calories    NUMERIC(8, 2) NOT NULL DEFAULT 0,
    meal_date   DATE          NOT NULL,
    meal_type   VARCHAR(16)   NOT NULL DEFAULT 'lunch',
    remark      VARCHAR(500),
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ   NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ   NOT NULL DEFAULT now(),
    del_flag    SMALLINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_meal PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_meal_user_date ON meal (user_id, meal_date) WHERE del_flag = 0;

COMMENT ON TABLE meal IS '饮食记录';
COMMENT ON COLUMN meal.meal_type IS 'breakfast/lunch/dinner/snack';

-- ----------------------------
-- 初始化基础食物分类
-- ----------------------------
INSERT INTO food_category (id, name, icon, sort, create_time, update_time) VALUES
(1761400000000000001, '主食',   '🍚', 1, now(), now()),
(1761400000000000002, '肉类',   '🥩', 2, now(), now()),
(1761400000000000003, '蔬菜',   '🥦', 3, now(), now()),
(1761400000000000004, '水果',   '🍎', 4, now(), now()),
(1761400000000000005, '乳制品', '🥛', 5, now(), now()),
(1761400000000000006, '坚果',   '🥜', 6, now(), now()),
(1761400000000000007, '饮料',   '🧃', 7, now(), now()),
(1761400000000000008, '其他',   '🍱', 8, now(), now())
ON CONFLICT DO NOTHING;

-- 常用食物（鸡胸肉）
INSERT INTO food (id, name, category_id, calories, protein, fat, carbs, unit, source, create_time, update_time) VALUES
(1761500000000000001, '鸡胸肉', 1761400000000000002, 133, 26.7, 2.5, 0, 'g', 'manual', now(), now())
ON CONFLICT DO NOTHING;
