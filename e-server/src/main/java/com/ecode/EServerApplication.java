package com.ecode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.ecode.mapper")
public class EServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EServerApplication.class, args);
    }

}
