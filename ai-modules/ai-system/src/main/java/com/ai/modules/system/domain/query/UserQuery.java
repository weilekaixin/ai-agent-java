package com.ai.modules.system.domain.query;

import com.ai.modules.system.domain.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户操作入参定义
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends User implements Serializable {
    /**
     * 用户名长度
     *
     * @see User#getAccount()
     */
    public static final int ACCOUNT_LEN = 20;
}
