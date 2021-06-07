package com.atguigu.guli.service.edu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})  //扫描所有的component
@EnableDiscoveryClient
public class ServiceEduApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class, args);
    }
}
