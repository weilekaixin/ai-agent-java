package com.ai.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 登录来源
 *
 * @author zhangyunlong
 */
@Getter
@AllArgsConstructor
public enum LoginSource {

    PC(1, "PC"),
    APP(2, "APP"),
    XCX(3, "小程序"),
    PAD(4, "平板"),
    ;

    private final Integer code;
    private final String name;

    public static LoginSource getByCode(Integer code) {
        return Arrays.stream(values()).filter(v -> v.getCode().equals(code)).findFirst().orElse(null);
    }
}
