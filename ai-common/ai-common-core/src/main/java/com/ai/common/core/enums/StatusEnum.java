package com.ai.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态
 *
 * @author zhangyunlong
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    NORMAL(0, "正常"),
    DISABLED(1, "禁用"),
    ;

    private final Integer code;
    private final String name;

    public static StatusEnum getByCode(Integer code) {
        return Arrays.stream(values()).filter(v -> v.getCode().equals(code)).findFirst().orElse(null);
    }
}
