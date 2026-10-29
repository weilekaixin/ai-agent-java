package com.ai.modules.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 记账服务启动类
 */
@SpringBootApplication
public class AccountingApplication {
    /**
     * 启动记账服务
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }
}
