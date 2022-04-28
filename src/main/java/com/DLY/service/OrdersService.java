package com.DLY.service;

import com.DLY.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86178
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-04-26 19:37:21
*/
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

    void again(Orders orders);
}
