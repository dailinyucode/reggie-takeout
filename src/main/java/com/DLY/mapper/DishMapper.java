package com.DLY.mapper;

import com.DLY.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-04-23 18:54:30
* @Entity com.DLY.pojo.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




