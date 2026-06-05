package com.ai.modules.nutrition.controller;

import com.ai.common.core.domain.R;
import com.ai.modules.nutrition.domain.entity.FoodCategory;
import com.ai.modules.nutrition.service.FoodCategoryService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 食物分类接口
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @GetMapping("/list")
    @SentinelResource("category:list")
    public R<List<FoodCategory>> list() {
        return R.ok(foodCategoryService.listAll());
    }

    @PostMapping
    public R<Void> add(@RequestBody FoodCategory category) {
        foodCategoryService.add(category);
        return R.ok();
    }

    @PutMapping
    public R<Void> update(@RequestBody FoodCategory category) {
        foodCategoryService.update(category);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        foodCategoryService.remove(id);
        return R.ok();
    }
}
