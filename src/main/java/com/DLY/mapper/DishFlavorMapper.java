package com.DLY.mapper;

import com.DLY.pojo.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2022-04-24 15:23:35
* @Entity com.DLY.pojo.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




