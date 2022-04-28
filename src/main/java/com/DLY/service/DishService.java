package com.DLY.service;

import com.DLY.common.R;
import com.DLY.dto.DishDto;
import com.DLY.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86178
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-04-23 18:54:30
*/
public interface DishService extends IService<Dish> {
    //新增菜品，插入口味数据，需要操作两两张表:dish,dish_flavor
    public void saveWithFiavor(DishDto dishDto);
    public R getone(Long id);
    public void updateWithFiavor(DishDto dishDto);

    void deleteByid(List<Long> id);
}
