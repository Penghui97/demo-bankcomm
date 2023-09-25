package com.bankcomm.demobankcomm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bankcomm.demobankcomm.mapper")
public class DemoBankCommApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBankCommApplication.class, args);
    }

}
