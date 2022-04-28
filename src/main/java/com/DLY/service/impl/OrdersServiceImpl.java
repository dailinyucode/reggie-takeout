package com.DLY.service.impl;

import com.DLY.common.BaseContext;
import com.DLY.common.CustomException;
import com.DLY.pojo.*;
import com.DLY.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.mapper.OrdersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author 86178
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2022-04-26 19:37:21
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    UserService userService;
    @Autowired
    AddressBookService addressBookService;


    /**
     * 用户下单 保存订单
     * @param orders
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        //获取当前用户id
        orders.setUserId(BaseContext.getCurrentId());
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,orders.getUserId());
        List<ShoppingCart> list =
                shoppingCartService.list(wrapper);
        if(list==null || list.size()==0){
            throw new CustomException("购物车为空,不能下单");
        }
        //查询地址数据
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook==null){
            throw new CustomException("购物车为空，不能下单");
        }
        //查询用户数据
        User user = userService.getById(orders.getUserId());
        //向订单表合订单明细表插入数据  1条

        //设置订单号
        Long orderId= IdWorker.getId();
        orders.setNumber(orderId.toString());
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        List<OrderDetail> orderDetails=new ArrayList<>();
        AtomicInteger amount=new AtomicInteger(0);
        list.forEach(v->{  //计算金额
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(v.getNumber());
            orderDetail.setDishId(v.getDishId());
            orderDetail.setDishFlavor(v.getDishFlavor());
            orderDetail.setSetmealId(v.getSetmealId());
            orderDetail.setName(v.getName());
            orderDetail.setImage(v.getImage());
            orderDetail.setAmount(v.getAmount());
            amount.addAndGet(v.getAmount().multiply(new BigDecimal(v.getNumber())).intValue());
            orderDetails.add(orderDetail);
        });
        orders.setAmount(new BigDecimal(amount.get()));  //计算金额
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(user.getPhone());
        orders.setAddress(addressBook.getDetail());
        this.save(orders);
        //向订单明细表插入数据   多条
        orderDetailService.saveBatch(orderDetails);
        //清除当前用户购物车
        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId,BaseContext.getCurrentId()));
    }

    /**
     * 完成的订单再一次定制
     * @param orders
     */
    @Override
    public void again(Orders orders) {
        List<OrderDetail> list = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, orders.getId()));
        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
        list.forEach(v->{
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(v,shoppingCart,"id","order_id");
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCarts.add(shoppingCart);
        });
        shoppingCartService.saveBatch(shoppingCarts);
    }
}




