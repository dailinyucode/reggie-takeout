package com.DLY.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.DishFlavor;
import com.DLY.service.DishFlavorService;
import com.DLY.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 86178
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-04-24 15:23:35
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




