package com.ai.modules.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录响应
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@AllArgsConstructor
public class LoginVO {

    /** Sa-Token 颁发的 token */
    private String token;
}
