package com.ai.modules.system.domain.query;

import com.ai.modules.system.domain.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户操作入参
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名长度
     */
    public static final int ACCOUNT_LEN = 20;

    /**
     * 旧密码（修改密码时使用）
     */
    private String oldPassword;

    /**
     * 新密码（重置/修改密码时使用）
     */
    private String newPassword;

    /**
     * ID 列表（批量操作时使用）
     */
    private List<Long> idList;
}
