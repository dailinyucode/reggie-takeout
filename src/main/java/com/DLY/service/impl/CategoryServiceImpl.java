package com.DLY.service.impl;

import com.DLY.common.CustomException;
import com.DLY.mapper.DishMapper;
import com.DLY.mapper.SetmealMapper;
import com.DLY.pojo.Dish;
import com.DLY.pojo.Setmeal;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.Category;
import com.DLY.service.CategoryService;
import com.DLY.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 86178
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-04-23 16:58:30
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    DishMapper dishMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public void removeid(Long id) {
        //删除之前先去判断是否有关联的菜品和套餐
        int count1 = dishMapper.selectCount(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, id));
        if(count1>0){
            //表示有关联的菜品  不能删除
            throw new CustomException("当前分类下关联了菜品");

        }
        int count2 = setmealMapper.selectCount(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, id));
        if(count2>0){
            //表示有关联的套餐   不能删除
            throw new CustomException("当前分类下关联了套餐");
        }
        categoryMapper.deleteById(id);
    }
}




