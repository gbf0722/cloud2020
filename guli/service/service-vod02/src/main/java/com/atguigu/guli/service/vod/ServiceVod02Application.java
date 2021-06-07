package com.atguigu.guli.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
@ComponentScan({"com.atguigu.guli"})
public class ServiceVod02Application {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVod02Application.class, args);
    }
}
