package com.DLY.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * sql唯一异常 处理异常
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        //判断字符是否包含自指定的字符串
        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String msg = s[2].split("'")[1] + "以经存在了";
            return R.error(msg);
        }
        return R.error("其他未知错误");
    }
    /**
     * 菜品关联异常
     */
    @ExceptionHandler(CustomException.class)
    public R customRxception(CustomException ex){
        return R.error(ex.getMessage());
    }
}
