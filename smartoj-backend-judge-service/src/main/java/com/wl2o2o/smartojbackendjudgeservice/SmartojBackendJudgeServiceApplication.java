package com.wl2o2o.smartojbackendjudgeservice;

import com.wl2o2o.smartojbackendjudgeservice.message.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wl2o2o")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wl2o2o.smartojbackendserviceclient.service")
public class SmartojBackendJudgeServiceApplication {
    public static void main(String[] args) {
        // 初始化消息队列
        InitRabbitMq.doInit();
        SpringApplication.run(SmartojBackendJudgeServiceApplication.class, args);
    }

}
