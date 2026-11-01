package com.ai.modules.accounting.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 当前用户资金账户概览
 */
@Data
public class AccountSummaryVO {
    /**
     * 全部账户，包含隐藏账户
     */
    private List<AccountVO> accounts;

    /**
     * 全部账户账面余额之和，单位元
     */
    private BigDecimal totalBalance;
}
