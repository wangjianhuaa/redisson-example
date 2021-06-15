package com.example.redission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class RedissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedissionApplication.class, args);
    }

}
