package com.ai.modules.accounting.enums;

/**
 * 资金流水变动类型，枚举名称对应数据库取值
 */
public enum AccountChangeType {
    /**
     * 初始余额
     */
    OPENING,
    /**
     * 收入
     */
    INCOME,
    /**
     * 消费
     */
    EXPENSE,
    /**
     * 返现
     */
    CASHBACK,
    /**
     * 账户转账
     */
    TRANSFER,
    /**
     * 余额调整
     */
    ADJUSTMENT,
    /**
     * 冲销
     */
    REVERSAL
}
