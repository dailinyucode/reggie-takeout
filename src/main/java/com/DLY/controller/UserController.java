package com.DLY.controller;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.DLY.dto.OrdersDto;
import com.DLY.pojo.OrderDetail;
import com.DLY.pojo.Orders;
import com.DLY.pojo.User;
import com.DLY.service.AddressBookService;
import com.DLY.service.OrderDetailService;
import com.DLY.service.OrdersService;
import com.DLY.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    OrdersService ordersService;

    @PostMapping("/login")
    public R local(
            @RequestBody Map map,
            HttpSession session
            ){
        log.info(map.get("phone").toString());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,map.get("phone").toString());
        User one = userService.getOne(userLambdaQueryWrapper);
        if(one==null){
            return R.error("没有该用户");
        }
        if(!"56789".equals(map.get("code").toString())){
            return R.error("验证码错误！");
        }
        session.setAttribute("phone",map.get("phone"));
        session.setAttribute("userID",one.getId());
        return R.success(null);
    }
    @PostMapping("/loginout")
    public R loginout(HttpSession session){
        session.removeAttribute("phone");
        return R.success(null);
    }

}
