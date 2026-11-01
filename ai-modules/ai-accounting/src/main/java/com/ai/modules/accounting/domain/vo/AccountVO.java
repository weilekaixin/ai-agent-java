package com.ai.modules.accounting.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 资金账户信息
 */
@Data
public class AccountVO {
    /**
     * 资金账户 ID
     */
    private Long accountId;

    /**
     * 账户名称
     */
    private String name;

    /**
     * 当前账面余额，单位元
     */
    private BigDecimal balance;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

    /**
     * 是否隐藏
     */
    private Boolean hidden;
}
