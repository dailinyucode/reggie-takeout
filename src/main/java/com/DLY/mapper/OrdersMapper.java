package com.DLY.mapper;

import com.DLY.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2022-04-26 19:37:21
* @Entity com.DLY.pojo.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




