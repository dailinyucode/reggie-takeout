package com.DLY.mapper;

import com.DLY.pojo.SetmealDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 86178
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
* @createDate 2022-04-24 20:10:09
* @Entity com.DLY.pojo.SetmealDish
*/
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    public List<SetmealDish> getlistBySid(Long sid);
}




