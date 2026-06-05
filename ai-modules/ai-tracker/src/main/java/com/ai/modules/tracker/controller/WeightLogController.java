package com.ai.modules.tracker.controller;

import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.tracker.domain.entity.WeightLog;
import com.ai.modules.tracker.service.WeightLogService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 体重记录接口
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/weight")
@RequiredArgsConstructor
public class WeightLogController {

    private final WeightLogService weightLogService;

    @PostMapping
    @SentinelResource("weight:log")
    public R<Void> log(@RequestBody WeightLog weightLog) {
        weightLog.setUserId(LoginHelper.getUserId());
        weightLogService.log(weightLog);
        return R.ok();
    }

    @GetMapping("/recent")
    public R<List<WeightLog>> recent(@RequestParam(defaultValue = "30") int limit) {
        return R.ok(weightLogService.recent(LoginHelper.getUserId(), limit));
    }
}
