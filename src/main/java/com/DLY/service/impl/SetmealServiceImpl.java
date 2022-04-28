package com.DLY.service.impl;

import com.DLY.common.CustomException;
import com.DLY.dto.SetmealDto;
import com.DLY.pojo.SetmealDish;
import com.DLY.service.SetmealDishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.Setmeal;
import com.DLY.service.SetmealService;
import com.DLY.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
* @author 86178
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-04-24 20:09:43
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Autowired
    SetmealDishService setmealDishService;
    @Value("${reggie.path}")
    String path;

    @Transactional
    @Override
    public void saveSetmealAndDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(v->{
            v.setSetmealId(setmealDto.getId().toString());
        });
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteIds(List<Long> ids) {
        ids.forEach(v->{
            Setmeal setmeal = this.getById(v);
            if(!setmeal.getStatus().equals(0)){
                throw new CustomException("还在售卖中，不能删除！");
            }
            //删除图片
            String imgpath=path+ setmeal.getImage();
            new File(imgpath).delete();

            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,v);
            setmealDishService.remove(queryWrapper);


            this.removeById(v);
        });
    }

    @Transactional
    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        //更新Setmeal的数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.updateById(setmeal);
        //删除SetmealDish的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        //添加更新后的数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(v->{
            v.setSetmealId(setmeal.getId().toString());
        });
        setmealDishService.saveBatch(setmealDishes);
    }
}




