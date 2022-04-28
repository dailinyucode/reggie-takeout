package com.DLY.dto;

import com.DLY.pojo.Setmeal;
import com.DLY.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
