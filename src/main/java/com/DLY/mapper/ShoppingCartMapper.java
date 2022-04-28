package com.DLY.mapper;

import com.DLY.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2022-04-26 13:58:59
* @Entity com.DLY.pojo.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




