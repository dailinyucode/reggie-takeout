package com.DLY.controller;

import com.DLY.common.CustomException;
import com.DLY.common.R;
import com.DLY.mapper.CategoryMapper;
import com.DLY.pojo.Category;
import com.DLY.pojo.Dish;
import com.DLY.pojo.Setmeal;
import com.DLY.service.CategoryService;
import com.DLY.service.DishService;
import com.DLY.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R getall(
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ){
        Page<Category> categoryPage = new Page<>(page,pageSize);
        //排序
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);

        return R.success(categoryService.page(categoryPage,categoryLambdaQueryWrapper));
    }

    @PostMapping
    public R save(@RequestBody Category category){
        categoryService.save(category);
        return R.success(null);
    }

    @PutMapping
    public R update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success(null);
    }

    @DeleteMapping
    public R delete(
            @RequestParam("id") Long id
    ){
        categoryService.removeid(id);
        return R.success("删除成功");
    }

    /**
     * 返回菜品的类型数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R type(Category category){
        LambdaQueryWrapper<Category> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();

        objectLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType())
                .orderByAsc(Category::getSort);

        List<Category> list = categoryService.list(objectLambdaQueryWrapper);

        return R.success(list);
    }

}
