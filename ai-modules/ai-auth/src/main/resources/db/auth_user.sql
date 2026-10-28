-- MySQL 8.0.16 及以上版本，主键由 MyBatis-Plus 生成。
create table auth_user (
    user_id bigint primary key comment '用户 ID',
    username varchar(64) not null comment '登录用户名',
    password varchar(100) not null comment '密码摘要，不保存明文密码',
    role varchar(10) not null default 'USER' comment '账号角色：ADMIN 超管，USER 普通用户',
    enabled boolean not null default true comment '是否启用，禁用后不允许登录',
    must_change_password boolean not null default true comment '是否必须修改初始密码',
    create_by bigint comment '创建者',
    create_time datetime not null default current_timestamp comment '创建时间',
    update_by bigint comment '更新者',
    update_time datetime default null comment '更新时间',
    constraint uk_auth_user_username unique (username),
    constraint ck_auth_user_role check (role in ('ADMIN', 'USER'))
) engine = InnoDB default charset = utf8mb4 collate = utf8mb4_bin comment = '认证用户表';
