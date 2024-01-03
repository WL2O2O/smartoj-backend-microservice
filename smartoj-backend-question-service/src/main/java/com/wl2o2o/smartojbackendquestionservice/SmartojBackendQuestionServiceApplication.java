package com.wl2o2o.smartojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wl2o2o.smartojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wl2o2o")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wl2o2o.smartojbackendserviceclient.service")
public class SmartojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartojBackendQuestionServiceApplication.class, args);
    }

}
