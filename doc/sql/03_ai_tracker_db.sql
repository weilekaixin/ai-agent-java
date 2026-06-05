-- 连接 ai_tracker_db 后执行
-- CREATE DATABASE ai_tracker_db;

-- ----------------------------
-- 体重记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS weight_log
(
    id          BIGINT        NOT NULL,
    user_id     BIGINT        NOT NULL,
    weight      NUMERIC(5, 2) NOT NULL,
    body_fat    NUMERIC(5, 2),
    log_date    DATE          NOT NULL,
    remark      VARCHAR(500),
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ   NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ   NOT NULL DEFAULT now(),
    del_flag    SMALLINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_weight_log PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_weight_log_user_date ON weight_log (user_id, log_date DESC) WHERE del_flag = 0;

COMMENT ON TABLE weight_log IS '体重体脂记录';

-- ----------------------------
-- 目标表
-- ----------------------------
CREATE TABLE IF NOT EXISTS goal
(
    id            BIGINT        NOT NULL,
    user_id       BIGINT        NOT NULL,
    goal_type     VARCHAR(32)   NOT NULL,
    target_value  NUMERIC(8, 2) NOT NULL,
    current_value NUMERIC(8, 2),
    deadline      DATE,
    status        VARCHAR(16)   NOT NULL DEFAULT 'in_progress',
    remark        VARCHAR(500),
    create_by     BIGINT,
    update_by     BIGINT,
    create_time   TIMESTAMPTZ   NOT NULL DEFAULT now(),
    update_time   TIMESTAMPTZ   NOT NULL DEFAULT now(),
    del_flag      SMALLINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_goal PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_goal_user ON goal (user_id) WHERE del_flag = 0;

COMMENT ON TABLE goal IS '目标管理';
COMMENT ON COLUMN goal.goal_type IS 'weight_loss/muscle_gain/calorie_control';
COMMENT ON COLUMN goal.status IS 'in_progress/achieved/abandoned';

-- ----------------------------
-- 每日卡路里聚合表（MQ 驱动写入）
-- ----------------------------
CREATE TABLE IF NOT EXISTS daily_calorie_summary
(
    id             BIGINT        NOT NULL,
    user_id        BIGINT        NOT NULL,
    summary_date   DATE          NOT NULL,
    total_calories NUMERIC(8, 2) NOT NULL DEFAULT 0,
    update_time    TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT pk_daily_calorie_summary PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_daily_calorie_user_date
    ON daily_calorie_summary (user_id, summary_date);

COMMENT ON TABLE daily_calorie_summary IS '每日卡路里聚合（由饮食记录MQ事件驱动）';
