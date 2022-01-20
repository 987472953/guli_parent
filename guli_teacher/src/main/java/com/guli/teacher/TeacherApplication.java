package com.guli.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients // 调用者
@SpringBootApplication
public class TeacherApplication {

    public static void main(String[] args) {

        SpringApplication.run(TeacherApplication.class, args);
    }
}
