package com.DLY.service;

import com.DLY.dto.SetmealDto;
import com.DLY.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86178
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-04-24 20:09:43
*/
public interface SetmealService extends IService<Setmeal> {

    void saveSetmealAndDish(SetmealDto setmealDto);

    void deleteIds(List<Long> ids);

    void updateSetmeal(SetmealDto setmealDto);
}
