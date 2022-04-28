package com.DLY.service.impl;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.DLY.dto.DishDto;
import com.DLY.dto.OrdersDto;
import com.DLY.pojo.OrderDetail;
import com.DLY.pojo.Orders;
import com.DLY.service.AddressBookService;
import com.DLY.service.OrderDetailService;
import com.DLY.service.OrdersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.User;
import com.DLY.service.UserService;
import com.DLY.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86178
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-04-25 21:10:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




