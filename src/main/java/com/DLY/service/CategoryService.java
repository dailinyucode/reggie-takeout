package com.DLY.service;

import com.DLY.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86178
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-04-23 16:58:30
*/
public interface CategoryService extends IService<Category> {

    void removeid(Long id);
}
