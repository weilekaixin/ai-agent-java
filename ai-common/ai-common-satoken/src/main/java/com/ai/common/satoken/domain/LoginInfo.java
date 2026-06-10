package com.ai.common.satoken.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录信息（Token 会话级别）
 *
 * @author zhangyunlong
 */
@Data
public class LoginInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 登录来源（1=PC 2=APP 3=小程序 4=平板）
     */
    private String loginSource;

    /**
     * 设备识别码
     */
    private String devKey;

    /**
     * 客户端ID
     */
    private String clientId;
}
