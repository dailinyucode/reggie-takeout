package com.DLY.controller;

import com.DLY.common.R;
import com.DLY.dto.DishDto;
import com.DLY.dto.SetmealDto;
import com.DLY.mapper.SetmealMapper;
import com.DLY.pojo.Category;
import com.DLY.pojo.Dish;
import com.DLY.pojo.Setmeal;
import com.DLY.pojo.SetmealDish;
import com.DLY.service.CategoryService;
import com.DLY.service.DishService;
import com.DLY.service.SetmealDishService;
import com.DLY.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealService setmealService;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    DishService dishService;

    @GetMapping("/page")
    public R getall(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(value = "name",required = false) String name
    ){
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);

        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        /**
         * 由于字段重复问题不适用sql语句了
         */
//        return R.success(setmealMapper.fandall(setmealDtoPage,setmealLambdaQueryWrapper));

        //查询完消息已经封装进去了
        setmealService.page(setmealPage, setmealLambdaQueryWrapper);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list=new ArrayList<>();
        records.forEach(v->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(v,setmealDto);
            Category byId = categoryService.getById(v.getCategoryId());
            setmealDto.setCategoryName(byId.getName());
            list.add(setmealDto);
        });
        //封装好的list集合放入page对象中
        setmealDtoPage.setRecords(list);
        return  R.success(setmealDtoPage);
    }
    @GetMapping("/{id}")
    public R getone(
          @PathVariable Long id
    ){
        return R.success(setmealMapper.getontDto(id));
    }

    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping
    public R save(
            @RequestBody SetmealDto setmealDto
    ){
        setmealService.saveSetmealAndDish(setmealDto);
        return R.success(null);
    }

    @CacheEvict(value = "setmealCache",allEntries = true)
    @DeleteMapping
    public R delete(
            @RequestParam List<Long> ids
            ){

        setmealService.deleteIds(ids);
        return R.success(null);
    }

    @PostMapping("/status/{statu}")
    public R status(
            @RequestParam List<Long> ids,
            @PathVariable Integer statu
    ){
        ids.forEach(v->{
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<Setmeal>();
            updateWrapper.eq(Setmeal::getId,v).set(Setmeal::getStatus,statu);
            setmealService.update(updateWrapper);
        });
        return R.success(null);
    }

    /**
     * allEntries删除这个分类下面的所有数据
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PutMapping
    public R put(
            @RequestBody SetmealDto setmealDto
    ){
        setmealService.updateSetmeal(setmealDto);

        return R.success(null);
    }

    @Cacheable(value = "setmealCache",key = "#categoryId+'_'+#status")
    @GetMapping("list")
    public R list(
            @RequestParam Long categoryId,
            @RequestParam Integer status
    ){
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);

        List<Setmeal> list = setmealService.list(setmealWrapper);
//        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();
//        list.forEach(v->{
//            SetmealDto setmealDto = new SetmealDto();
//            BeanUtils.copyProperties(v,setmealDto);
//
//            //获取套餐下的订单信息
//            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
//            List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);
//            setmealDto.setSetmealDishes(setmealDishes);
//
//
//            setmealDtos.add(setmealDto);
//
//
//        });
        return R.success(list);
    }
    @GetMapping("/dish/{setmealid}")
    public R getdishlist(
            @PathVariable Long setmealid
    ){
        List<SetmealDish> list = setmealDishService.list(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealid));
        ArrayList<DishDto> dishDtos = new ArrayList<DishDto>();
        list.forEach(v->{
            Dish dish = dishService.getById(v.getDishId());
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            //设置份数
            dishDto.setCopies(v.getCopies());

            dishDtos.add(dishDto);
        });

        return R.success(dishDtos);
    }
}
