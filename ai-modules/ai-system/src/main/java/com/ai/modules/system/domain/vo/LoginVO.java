package com.ai.modules.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录响应
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
public class LoginVO {

    /**
     * 会话标识（token）
     */
    private String token;

    /**
     * 会话标识名称
     */
    private String tokenName;

    /**
     * 客户端ID
     */
    private String clientId;
}
