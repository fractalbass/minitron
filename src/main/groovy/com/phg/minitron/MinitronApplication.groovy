package com.phg.minitron

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan(basePackages = ["com.phg.minitron.dao", "com.phg.minitron.service", "com.phg.minitron.controller"])
@SpringBootApplication
public class MinitronApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinitronApplication.class, args)
    }

}

