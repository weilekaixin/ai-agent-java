package com.ai.modules.nutrition.controller;

import com.ai.common.core.domain.R;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.modules.nutrition.domain.entity.Food;
import com.ai.modules.nutrition.service.FoodService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 食物库接口
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/page")
    @SentinelResource("food:page")
    public R<IPage<Food>> page(String name, Long categoryId, PageQuery pageQuery) {
        return R.ok(foodService.page(name, categoryId, pageQuery));
    }

    @GetMapping("/{id}")
    public R<Food> getById(@PathVariable Long id) {
        return R.ok(foodService.getById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody Food food) {
        foodService.add(food);
        return R.ok();
    }

    @PutMapping
    public R<Void> update(@RequestBody Food food) {
        foodService.update(food);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        foodService.remove(id);
        return R.ok();
    }
}
