package com.DLY.service.impl;

import com.DLY.common.CustomException;
import com.DLY.common.GlobalExceptionHandler;
import com.DLY.common.R;
import com.DLY.dto.DishDto;
import com.DLY.mapper.DishMapper;
import com.DLY.pojo.Category;
import com.DLY.pojo.Dish;
import com.DLY.pojo.DishFlavor;
import com.DLY.service.CategoryService;
import com.DLY.service.DishFlavorService;
import com.DLY.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    @Value("${reggie.path}")
    private String bashPath;

    @Transactional
    @Override
    public void saveWithFiavor(DishDto dishDto) {
        //由于disDto是它的子类 所以可以插入
        dishMapper.insert(dishDto);
        //插入完成之后就拥有dishid
        Long id = dishDto.getId();
        dishDto.getFlavors().forEach(v->{
            v.setDishId(id);
        });
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public R getone(Long id) {
        DishDto dishDto = new DishDto();
        //查询进行复制
        Dish dish = dishMapper.selectById(id);
        BeanUtils.copyProperties(dish,dishDto);

        //通过dish的dish.id去查询dishFlavor信息
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list =
                dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(list);
        //通过categoryId去查询categoryName
        Category category = categoryService.getById(dish.getCategoryId());
        dishDto.setCategoryName(category.getName());

        return R.success(dishDto);
    }


    @Transactional
    @Override
    public void updateWithFiavor(DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        dishMapper.updateById(dish);
        //新集合
        List<DishFlavor> newlist = dishDto.getFlavors();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        //原来的老集合
        List<DishFlavor> oldlist = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        /**
         * 这里进行修改有更好的办法删除原来的，添加新来的
         */
        newlist.forEach(v->{
            Long dishFlavorId=v.getId();
            if(dishFlavorId==null){   //没有自己的id一定是添加
                v.setDishId(dish.getId());
                dishFlavorService.save(v);
            }
        });
        for (int i = 0; i < oldlist.size(); i++) {   //对比新旧元素，对没有老的元素进行删除
            int j=0;
            for (; j < newlist.size(); j++) {
                if(oldlist.get(i).getId().equals(newlist.get(j).getId())){
                    dishFlavorService.updateById(newlist.get(j));
                    break;
                }
            }
            if(j==newlist.size()){
                dishFlavorService.removeById(oldlist.get(i).getId());
            }
        }
    }

    @Transactional
    @Override
    public void deleteByid(List<Long> id) {
        id.forEach(i->{
            Dish dish = this.getById(i);
            //先去查看口味里面是否有相关联的表
            List<DishFlavor> list = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dish.getId()));
            if(list.size()!=0){
                throw new CustomException("关联了其他的表，不能删！");
            }
            //删除图片
            String image = dish.getImage();
            File file = new File(bashPath + image);
            file.delete();
            this.removeById(i);
        });
    }
}
