package com.ai.modules.nutrition.controller;

import com.ai.common.core.domain.R;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.nutrition.domain.entity.Meal;
import com.ai.modules.nutrition.service.MealService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录接口
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    /**
     * 记录饮食（触发 MQ 事件）
     */
    @PostMapping
    @SentinelResource("meal:log")
    public R<Meal> log(@RequestBody Meal meal) {
        meal.setUserId(LoginHelper.getUserId());
        return R.ok(mealService.log(meal));
    }

    /**
     * 分页查询我的饮食记录
     *
     * @param date 日期，不传默认今天
     */
    @GetMapping("/page")
    public R<IPage<Meal>> page(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            PageQuery pageQuery) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        return R.ok(mealService.pageByDate(LoginHelper.getUserId(), queryDate, pageQuery));
    }

    /**
     * 查询今日总热量
     */
    @GetMapping("/today/calories")
    public R<BigDecimal> todayCalories() {
        return R.ok(mealService.todayCalories(LoginHelper.getUserId(), LocalDate.now()));
    }

    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        mealService.remove(id);
        return R.ok();
    }
}
