package com.ai.modules.tracker.controller;

import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.tracker.domain.entity.Goal;
import com.ai.modules.tracker.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目标接口
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public R<Void> create(@RequestBody Goal goal) {
        goal.setUserId(LoginHelper.getUserId());
        goalService.create(goal);
        return R.ok();
    }

    @GetMapping("/list")
    public R<List<Goal>> list() {
        return R.ok(goalService.listByUser(LoginHelper.getUserId()));
    }

    @PutMapping("/{id}/achieve")
    public R<Void> achieve(@PathVariable Long id) {
        goalService.achieve(id, LoginHelper.getUserId());
        return R.ok();
    }

    @PutMapping("/{id}/abandon")
    public R<Void> abandon(@PathVariable Long id) {
        goalService.abandon(id, LoginHelper.getUserId());
        return R.ok();
    }
}
