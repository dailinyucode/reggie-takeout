package com.DLY.mapper;

import com.DLY.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-04-23 16:58:30
* @Entity com.DLY.pojo.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




