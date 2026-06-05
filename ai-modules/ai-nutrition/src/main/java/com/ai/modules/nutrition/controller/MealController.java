package com.ai.modules.nutrition.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.nutrition.model.query.MealListQuery;
import com.ai.modules.nutrition.model.query.MealQuery;
import com.ai.modules.nutrition.service.MealService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 营养管理/饮食记录
 */
@RestController
@RequestMapping("/meal")
public class MealController {

    private static final String MODEL_NAME = "饮食记录";

    @Resource
    private MealService mealService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<?>> list(@Valid MealListQuery query) {
        return R.ok(mealService.listPage(LoginHelper.getUserId(), query));
    }

    /**
     * 今日总热量
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/today_calories")
    public R<BigDecimal> todayCalories() {
        return R.ok(mealService.todayCalories(LoginHelper.getUserId(), LocalDate.now()));
    }

    /**
     * 记录饮食
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    @SentinelResource("meal:add")
    public R<?> add(@Valid @RequestBody MealQuery query) {
        return mealService.log(LoginHelper.getUserId(), query);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody MealListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return mealService.delBatch(query.getIdList());
    }
}
