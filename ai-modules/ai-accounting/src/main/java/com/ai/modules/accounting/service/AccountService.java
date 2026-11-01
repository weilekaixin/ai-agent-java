package com.ai.modules.accounting.service;

import cn.hutool.core.util.StrUtil;
import com.ai.common.core.exception.BusinessException;
import com.ai.modules.accounting.domain.Account;
import com.ai.modules.accounting.domain.AccountFlow;
import com.ai.modules.accounting.domain.query.AccountQuery;
import com.ai.modules.accounting.domain.vo.AccountSummaryVO;
import com.ai.modules.accounting.domain.vo.AccountVO;
import com.ai.modules.accounting.enums.AccountChangeType;
import com.ai.modules.accounting.mapper.AccountFlowMapper;
import com.ai.modules.accounting.mapper.AccountMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 资金账户业务
 */
@Service
@Validated
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountFlowMapper accountFlowMapper;

    /**
     * 查询本人账户及总余额，一次查询保证列表与汇总使用同一份数据
     */
    public AccountSummaryVO getSummary(@NotNull @Positive Long userId) {
        var accounts = accountMapper.selectList(this.buildWrapper(userId));
        AccountSummaryVO vo = new AccountSummaryVO();
        vo.setAccounts(accounts.stream().map(this::toAccountVO).toList());
        vo.setTotalBalance(accounts.stream().map(Account::getBalance)
                .reduce(BigDecimal.ZERO.setScale(AccountQuery.AMOUNT_SCALE), BigDecimal::add));
        return vo;
    }

    /**
     * 构建本人账户查询条件，隐藏账户仍参与余额汇总
     */
    private LambdaQueryWrapper<Account> buildWrapper(Long userId) {
        return Wrappers.lambdaQuery(Account.class)
                .select(Account::getAccountId, Account::getName, Account::getBalance,
                        Account::getSortOrder, Account::getHidden)
                .eq(Account::getUserId, userId)
                .orderByAsc(Account::getSortOrder)
                .orderByAsc(Account::getAccountId);
    }

    /**
     * 创建账户及初始流水，userId 必须由调用方从登录上下文取得
     */
    @Transactional(rollbackFor = Exception.class)
    public AccountVO create(@NotNull @Positive Long userId, @NotNull @Valid AccountQuery query) {
        Account account = new Account();
        account.setUserId(userId);
        account.setName(StrUtil.trim(query.getName()));
        account.setBalance(query.getOpeningBalance().setScale(AccountQuery.AMOUNT_SCALE, RoundingMode.UNNECESSARY));
        account.setSortOrder(AccountQuery.DEFAULT_SORT_ORDER);
        account.setHidden(false);
        account.setCreateBy(userId);
        try {
            accountMapper.insert(account);
        } catch (DuplicateKeyException exception) {
            throw new BusinessException("账户名称已存在", HttpStatus.CONFLICT.value());
        }
        if (account.getBalance().signum() > 0) {
            this.createOpeningFlow(account);
        }
        return this.toAccountVO(account);
    }

    /**
     * 记录初始余额，零余额不生成资金变动
     */
    private void createOpeningFlow(Account account) {
        AccountFlow flow = new AccountFlow();
        flow.setUserId(account.getUserId());
        flow.setAccountId(account.getAccountId());
        flow.setChangeType(AccountChangeType.OPENING);
        flow.setChangeAmount(account.getBalance());
        flow.setBalanceAfter(account.getBalance());
        flow.setRequestKey("opening:" + account.getAccountId());
        flow.setOccurredTime(new Date());
        flow.setDetails("初始余额");
        flow.setCreateBy(account.getUserId());
        accountFlowMapper.insert(flow);
    }

    /**
     * 转换账户展示信息
     */
    private AccountVO toAccountVO(Account account) {
        AccountVO vo = new AccountVO();
        vo.setAccountId(account.getAccountId());
        vo.setName(account.getName());
        vo.setBalance(account.getBalance());
        vo.setSortOrder(account.getSortOrder());
        vo.setHidden(account.getHidden());
        return vo;
    }
}
