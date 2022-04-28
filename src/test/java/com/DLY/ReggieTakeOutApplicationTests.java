package com.DLY;

import com.DLY.mapper.EmployeeMapper;
import com.DLY.pojo.Employee;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ReggieTakeOutApplicationTests {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(employeeMapper.selectList(new QueryWrapper<Employee>()));
    }

    @Test
    void redisTest(){
        redisTemplate.boundValueOps("123").set("436");
        System.out.println();
    }
}
