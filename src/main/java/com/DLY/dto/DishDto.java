package com.DLY.dto;

import com.DLY.pojo.Dish;
import com.DLY.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    /**
     * 封装口味对象
     */
    private List<DishFlavor> flavors=new ArrayList<>();
    /**
     * 封装分类名字
     */
    private String categoryName;
    /**
     * 封装份数
     */
    private Integer copies;
}
