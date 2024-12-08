package com.ecode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@MapperScan("com.ecode.mapper")
public class EServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EServerApplication.class, args);
    }

}
