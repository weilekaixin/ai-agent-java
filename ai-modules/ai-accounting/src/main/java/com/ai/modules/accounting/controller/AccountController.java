package com.ai.modules.accounting.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.accounting.domain.query.AccountQuery;
import com.ai.modules.accounting.domain.vo.AccountSummaryVO;
import com.ai.modules.accounting.domain.vo.AccountVO;
import com.ai.modules.accounting.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前用户资金账户接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounting/accounts")
public class AccountController {
    private final AccountService accountService;

    /**
     * 查询本人全部资金账户及总余额
     */
    @GetMapping
    public R<AccountSummaryVO> getSummary() {
        return R.ok(accountService.getSummary(StpUtil.getLoginIdAsLong()));
    }

    /**
     * 新增本人资金账户并记录初始余额
     */
    @PostMapping
    public R<AccountVO> create(@Valid @RequestBody AccountQuery query) {
        return R.ok(accountService.create(StpUtil.getLoginIdAsLong(), query));
    }
}
