-- 连接 ai_user_db 后执行
-- CREATE DATABASE ai_user_db;

-- ----------------------------
-- 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user
(
    id          BIGINT                   NOT NULL,
    username    VARCHAR(64)              NOT NULL,
    nickname    VARCHAR(64),
    password    VARCHAR(255)             NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(100),
    avatar      VARCHAR(500),
    user_type   VARCHAR(20)              NOT NULL DEFAULT 'sys_user',
    status      SMALLINT                 NOT NULL DEFAULT 0,
    login_ip    VARCHAR(64),
    login_date  TIMESTAMPTZ,
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ              NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ              NOT NULL DEFAULT now(),
    del_flag    SMALLINT                 NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_user PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_sys_user_username
    ON sys_user (username) WHERE del_flag = 0;

COMMENT ON TABLE sys_user IS '系统用户';
COMMENT ON COLUMN sys_user.status IS '0=正常 1=禁用';
COMMENT ON COLUMN sys_user.del_flag IS '0=未删 1=已删';

-- ----------------------------
-- 角色表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role
(
    id          BIGINT                   NOT NULL,
    role_name   VARCHAR(64)              NOT NULL,
    role_key    VARCHAR(64)              NOT NULL,
    status      SMALLINT                 NOT NULL DEFAULT 0,
    remark      VARCHAR(500),
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ              NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ              NOT NULL DEFAULT now(),
    del_flag    SMALLINT                 NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_role PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_sys_role_key
    ON sys_role (role_key) WHERE del_flag = 0;

COMMENT ON TABLE sys_role IS '系统角色';
COMMENT ON COLUMN sys_role.role_key IS '角色标识，如 superadmin';

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_sys_user_role PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE sys_user_role IS '用户角色关联';

-- ----------------------------
-- 权限表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_permission
(
    id          BIGINT                   NOT NULL,
    perm_key    VARCHAR(128)             NOT NULL,
    perm_name   VARCHAR(64)              NOT NULL,
    remark      VARCHAR(500),
    create_by   BIGINT,
    update_by   BIGINT,
    create_time TIMESTAMPTZ              NOT NULL DEFAULT now(),
    update_time TIMESTAMPTZ              NOT NULL DEFAULT now(),
    del_flag    SMALLINT                 NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_permission PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_sys_permission_key
    ON sys_permission (perm_key) WHERE del_flag = 0;

COMMENT ON TABLE sys_permission IS '系统权限';
COMMENT ON COLUMN sys_permission.perm_key IS '权限标识，如 system:user:list';

-- ----------------------------
-- 角色权限关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role_permission
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    CONSTRAINT pk_sys_role_permission PRIMARY KEY (role_id, permission_id)
);

COMMENT ON TABLE sys_role_permission IS '角色权限关联';

-- ----------------------------
-- 初始化超级管理员（密码: Admin@123，BCrypt 加密）
-- ----------------------------
INSERT INTO sys_user (id, username, nickname, password, user_type, status, create_time, update_time)
VALUES (1761100000000000001,
        'admin',
        '超级管理员',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
        'sys_user',
        0,
        now(),
        now())
ON CONFLICT DO NOTHING;

INSERT INTO sys_role (id, role_name, role_key, status, create_time, update_time)
VALUES (1761300000000000001, '超级管理员', 'superadmin', 0, now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO sys_user_role (user_id, role_id)
VALUES (1761100000000000001, 1761300000000000001)
ON CONFLICT DO NOTHING;
