-- 连接 ai_training_db 后执行
-- CREATE DATABASE ai_training_db;

-- ----------------------------
-- 运动动作库
-- ----------------------------
CREATE TABLE IF NOT EXISTS exercise
(
    id               BIGINT       NOT NULL,
    name             VARCHAR(128) NOT NULL,
    muscle_group     VARCHAR(32)  NOT NULL,
    equipment_type   VARCHAR(32),
    calories_per_min INT          NOT NULL DEFAULT 0,
    description      TEXT,
    create_by        BIGINT,
    update_by        BIGINT,
    create_time      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    update_time      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    del_flag         SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_exercise PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS udx_exercise_name ON exercise (name) WHERE del_flag = 0;

COMMENT ON TABLE exercise IS '运动动作库';
COMMENT ON COLUMN exercise.muscle_group IS '肌肉群：chest/back/legs/shoulders/arms/core/cardio';
COMMENT ON COLUMN exercise.equipment_type IS '器械类型：barbell/dumbbell/machine/bodyweight/cable';
COMMENT ON COLUMN exercise.calories_per_min IS '每分钟消耗热量估算（kcal）';

-- ----------------------------
-- 训练会话（一次完整训练记录）
-- ----------------------------
CREATE TABLE IF NOT EXISTS training_session
(
    id              BIGINT        NOT NULL,
    user_id         BIGINT        NOT NULL,
    title           VARCHAR(128),
    train_date      DATE          NOT NULL,
    start_time      TIMESTAMPTZ,
    end_time        TIMESTAMPTZ,
    duration_min    INT           NOT NULL DEFAULT 0,
    calories_burned NUMERIC(8, 2) NOT NULL DEFAULT 0,
    status          VARCHAR(32)   NOT NULL DEFAULT 'in_progress',
    remark          VARCHAR(512),
    create_by       BIGINT,
    update_by       BIGINT,
    create_time     TIMESTAMPTZ   NOT NULL DEFAULT now(),
    update_time     TIMESTAMPTZ   NOT NULL DEFAULT now(),
    del_flag        SMALLINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_training_session PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_training_session_user_date ON training_session (user_id, train_date) WHERE del_flag = 0;

COMMENT ON TABLE training_session IS '训练会话';
COMMENT ON COLUMN training_session.status IS '状态：in_progress/completed';

-- ----------------------------
-- 训练组（会话下的每组动作）
-- ----------------------------
CREATE TABLE IF NOT EXISTS training_set
(
    id          BIGINT        NOT NULL,
    session_id  BIGINT        NOT NULL,
    exercise_id BIGINT        NOT NULL,
    set_order   INT           NOT NULL DEFAULT 1,
    weight      NUMERIC(8, 2) NOT NULL DEFAULT 0,
    reps        INT           NOT NULL DEFAULT 0,
    done        SMALLINT      NOT NULL DEFAULT 0,
    remark      VARCHAR(512),
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ   NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ   NOT NULL DEFAULT now(),
    del_flag    SMALLINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_training_set PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_training_set_session ON training_set (session_id) WHERE del_flag = 0;

COMMENT ON TABLE training_set IS '训练组';
COMMENT ON COLUMN training_set.done IS '完成状态：0=计划 1=完成';

-- ----------------------------
-- 训练计划（可复用模板）
-- ----------------------------
CREATE TABLE IF NOT EXISTS training_plan
(
    id              BIGINT      NOT NULL,
    user_id         BIGINT      NOT NULL,
    name            VARCHAR(128) NOT NULL,
    target_muscles  VARCHAR(256),
    estimated_min   INT          NOT NULL DEFAULT 0,
    difficulty      VARCHAR(32),
    remark          VARCHAR(512),
    create_by       BIGINT,
    update_by       BIGINT,
    create_time     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    update_time     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    del_flag        SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_training_plan PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_training_plan_user ON training_plan (user_id) WHERE del_flag = 0;

COMMENT ON TABLE training_plan IS '训练计划（模板）';
COMMENT ON COLUMN training_plan.difficulty IS '难度：beginner/intermediate/advanced';

-- ----------------------------
-- 内置常用动作数据
-- ----------------------------
INSERT INTO exercise (id, name, muscle_group, equipment_type, calories_per_min, description) VALUES
(1761200000000000001, '卧推',       'chest',     'barbell',    5,  '胸部基础力量动作'),
(1761200000000000002, '深蹲',       'legs',      'barbell',    6,  '下肢综合力量动作'),
(1761200000000000003, '硬拉',       'back',      'barbell',    7,  '全身复合动作'),
(1761200000000000004, '引体向上',   'back',      'bodyweight', 5,  '上背部拉力动作'),
(1761200000000000005, '哑铃飞鸟',   'chest',     'dumbbell',   4,  '胸部孤立动作'),
(1761200000000000006, '跑步机',     'cardio',    'machine',    10, '有氧心肺训练'),
(1761200000000000007, '平板支撑',   'core',      'bodyweight', 4,  '核心稳定性训练'),
(1761200000000000008, '肩部推举',   'shoulders', 'dumbbell',   5,  '三角肌综合动作')
ON CONFLICT DO NOTHING;
