package com.qiyue.meteor.code.factory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * created by yebinghuan on 2018/4/17
 */
@SpringBootApplication
@MapperScan("com.github.qiyue.code.factory.mapper") //mybatis扫描注解
public class CodeFactoryApplicationBootStrap {

    public static void main(String[] args) {

        SpringApplication.run(CodeFactoryApplicationBootStrap.class,args);

    }
}
