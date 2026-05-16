package com.ai.common.json.validate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JSON 类型枚举
 *
 * @author root 2026-05-16 16:04
 */
@Getter
@AllArgsConstructor
public enum JsonType {

    /**
     * JSON 对象，例如 {"a":1}
     */
    OBJECT,

    /**
     * JSON 数组，例如 [1,2,3]
     */
    ARRAY,

    /**
     * 任意 JSON 类型，对象或数组都可以
     */
    ANY

}
