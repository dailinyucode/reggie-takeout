package com.DLY.controller;

import com.DLY.common.R;
import com.DLY.dto.DishDto;
import com.DLY.pojo.Category;
import com.DLY.pojo.Dish;
import com.DLY.pojo.DishFlavor;
import com.DLY.service.CategoryService;
import com.DLY.service.DishFlavorService;
import com.DLY.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/page")
    public R getall(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(value = "name",required = false) String name
    ){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getCreateTime);
        //执行查询
        dishService.page(dishPage,queryWrapper);
        //对象拷贝  页面 分页信息
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> list=new ArrayList<DishDto>();
        records.forEach(v->{
            DishDto dishDto=new DishDto();
            //拷贝数据
            BeanUtils.copyProperties(v,dishDto);

            //通过Dish菜品拿到菜品分类id
            Long categoryId = v.getCategoryId();
            //根据id查询分类对象
            Category byId = categoryService.getById(categoryId);
            //拿到分类名称
            String name1 = byId.getName();

            dishDto.setCategoryName(name1);
            list.add(dishDto);
        });

        //设置Page的内容
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R sava(
            @RequestBody DishDto dishDto
            ){
        Set keys = redisTemplate.keys("dish_" + dishDto.getCategoryId().toString()+"_1");
        redisTemplate.delete(keys);

        dishService.saveWithFiavor(dishDto);
        return R.success(null);
    }

    @PutMapping
    public R put(
            @RequestBody DishDto dishDto
    ){
        //删除所有
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        //精确清理
        Set keys = redisTemplate.keys("dish_" + dishDto.getCategoryId().toString()+"_1");
        redisTemplate.delete(keys);

        dishService.updateWithFiavor(dishDto);
       return R.success(null);
    };

    /**
     * 通过id 修改状态码
     * @param ids
     * @return
     */
    @PostMapping("/status/{num}")
    public R status(
            @PathVariable("num") Integer num,
            @RequestParam("ids") List<Long> ids
    ){
        ids.forEach(v->{
            LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            dishLambdaUpdateWrapper.eq(Dish::getId,v).set(Dish::getStatus,num);
            dishService.update(dishLambdaUpdateWrapper);
        });
        return R.success(null);
    }

    /**
     * 通过id删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(
            @RequestParam("ids") List<Long> ids
    ){
        dishService.deleteByid(ids);
        return R.success(null);
    }

    /**
     * 通过id获取数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(
            @PathVariable Long id
    ){
        return dishService.getone(id);
    }

    /**
     * 这个是获取这个类别下的所有菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R getlist(
            @RequestParam Long categoryId,
            @RequestParam Integer status
    ){

        String key="dish_"+categoryId+"_"+status;
        //从redis中获取缓存数据
        List<DishDto> dish =(List<DishDto>)redisTemplate.opsForValue().get(key);
        //log.info(dish.toString());
        //如果存在，直接返回 无需查询数据库
        if(dish!=null){
            return R.success(dish);
        }

        //不存在再去数据库查询，并保存的redis
        List<Dish> list = dishService.list(new LambdaQueryWrapper<Dish>()
                //保证是起售状态的
                .eq(Dish::getCategoryId, categoryId).eq(Dish::getStatus, 1));

        List<DishDto> dishDtoList = new ArrayList<>();

        list.forEach(v->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(v,dishDto);

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,v.getId());
            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(flavors);

            dishDtoList.add(dishDto);
        });

        redisTemplate.opsForValue().set(key,dishDtoList,60,TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
}
