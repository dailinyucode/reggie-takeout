package com.DLY;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement  //开启事务支持
public class ReggieTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
    }

}
