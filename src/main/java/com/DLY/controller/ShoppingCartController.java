package com.DLY.controller;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.DLY.pojo.ShoppingCart;
import com.DLY.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R list(){
        return R.success(shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>().orderByAsc(ShoppingCart::getCreateTime)));
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R add(
            @RequestBody ShoppingCart shoppingCart
            ){
        //设置用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //查询套餐是否添加过
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if(one==null){
            shoppingCartService.save(shoppingCart);
        }else{
            shoppingCartService.update(new LambdaUpdateWrapper<ShoppingCart>().set(ShoppingCart::getNumber,one.getNumber()+1));
        }
        return R.success(shoppingCart);
    }

    /**
     * 删除单个
     * @param map
     * @return
     */
    @PostMapping("/sub")
    public R delete(
            @RequestBody Map<String,Long> map
            ){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(map.get("dishId")!=null,ShoppingCart::getDishId,map.get("dishId"))
                .eq(map.get("setmealId")!=null,ShoppingCart::getSetmealId,map.get("setmealId"));
        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        one.setNumber(one.getNumber()-1);
        if(one.getNumber()>=1){
            shoppingCartService.updateById(one);
        }else{
            shoppingCartService.removeById(one.getId());
        }
        return R.success(one);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("clean")
    public R deletelist(){
        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId,BaseContext.getCurrentId()));
        return R.success(null);
    }
}
