package com.ai.modules.tracker.controller;

import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.tracker.domain.entity.DailyCalorieSummary;
import com.ai.modules.tracker.mapper.DailyCalorieSummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 今日总览接口（供 Python Agent 调用）
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/tracker")
@RequiredArgsConstructor
public class TrackerController {

    private final DailyCalorieSummaryMapper summaryMapper;

    /**
     * 获取今日卡路里摄入汇总
     */
    @GetMapping("/today")
    public R<DailyCalorieSummary> today() {
        DailyCalorieSummary summary = summaryMapper.selectByUserAndDate(
                LoginHelper.getUserId(), LocalDate.now());
        return R.ok(summary);
    }
}
