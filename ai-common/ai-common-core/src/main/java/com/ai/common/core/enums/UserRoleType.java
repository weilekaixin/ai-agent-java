package com.ai.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户角色类型
 *
 * @author zhangyunlong
 */
@Getter
@AllArgsConstructor
public enum UserRoleType {

    SUPER_ADMIN(1, "超级管理员"),
    SUB_ADMIN(2, "子管理员"),
    GENERAL(3, "普通用户"),
    ;

    private final Integer code;
    private final String name;

    public static UserRoleType getByCode(Integer code) {
        return Arrays.stream(values()).filter(v -> v.getCode().equals(code)).findFirst().orElse(null);
    }

    /**
     * 是否为超级管理员
     */
    public static boolean isSuperAdmin(Integer code) {
        return SUPER_ADMIN.getCode().equals(code);
    }

    /**
     * 是否为管理员（含子管理员）
     */
    public static boolean isAdmin(Integer code) {
        return code != null && (SUPER_ADMIN.getCode().equals(code) || SUB_ADMIN.getCode().equals(code));
    }
}
