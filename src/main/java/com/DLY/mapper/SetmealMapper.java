package com.DLY.mapper;

import com.DLY.dto.SetmealDto;
import com.DLY.pojo.Setmeal;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86178
* @description 针对表【setmeal(套餐)】的数据库操作Mapper
* @createDate 2022-04-24 20:09:43
* @Entity com.DLY.pojo.Setmeal
*/
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    public IPage<SetmealDto> fandall(@Param("page")IPage<SetmealDto> ppge,@Param(Constants.WRAPPER) Wrapper<Setmeal> queryWrapper);
    public SetmealDto getontDto(Long id);
}




