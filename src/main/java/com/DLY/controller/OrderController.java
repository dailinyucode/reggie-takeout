package com.DLY.controller;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.DLY.dto.OrdersDto;
import com.DLY.pojo.OrderDetail;
import com.DLY.pojo.Orders;
import com.DLY.service.AddressBookService;
import com.DLY.service.OrderDetailService;
import com.DLY.service.OrdersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrdersService ordersService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R add(
            @RequestBody Orders orders
            ){
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R page(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Long number,
            @RequestParam(required=false)String beginTime,
            @RequestParam(required = false)String endTime
    ){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(number!=null,Orders::getId,number)
                .between(beginTime!=null&&endTime!=null,Orders::getOrderTime,beginTime,endTime);

        return R.success(ordersService.page(ordersPage,wrapper));
    }

    @PostMapping("/again")
    public R again(
            @RequestBody Orders orders
    ){
        ordersService.again(orders);
        return R.success("下单成功");
    }

    @PutMapping
    public R put(
            @RequestBody Orders orders
    ){
        ordersService.updateById(orders);
        return R.success(null);
    }
    @GetMapping("/userPage")
    public R userpage(
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ){
        Page<OrdersDto> ordersDtoPage = new Page<>(page,pageSize);
        Page<Orders> ordersPage=new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentId())
                        .orderByDesc(Orders::getOrderTime);

        ordersService.page(ordersPage, wrapper);
        BeanUtils.copyProperties(ordersPage,ordersDtoPage,"records");
        ArrayList<OrdersDto> dishDtos = new ArrayList<>();
        List<Orders> records =
                ordersPage.getRecords();

        records.forEach(v->{
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(v,ordersDto);

            LambdaQueryWrapper<OrderDetail> a = new LambdaQueryWrapper<>();
            a.eq(OrderDetail::getOrderId,v.getId());
            List<OrderDetail> list = orderDetailService.list(a);

            ordersDto.setOrderDetails(list);
            dishDtos.add(ordersDto);
        });
        ordersDtoPage.setRecords(dishDtos);

        return R.success(ordersDtoPage);
    }
}
