package com.DLY.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.OrderDetail;
import com.DLY.service.OrderDetailService;
import com.DLY.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 86178
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-04-26 19:37:25
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




