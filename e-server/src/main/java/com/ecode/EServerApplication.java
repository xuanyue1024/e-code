package com.ecode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@MapperScan("com.ecode.mapper")
@EnableAsync
public class EServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EServerApplication.class, args);
    }

}
