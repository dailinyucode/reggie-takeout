package com.DLY.mapper;

import com.DLY.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-04-26 19:37:25
* @Entity com.DLY.pojo.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




