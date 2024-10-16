package com.taidev198;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.taidev198.config.JwtApplicationProperty;

@SpringBootApplication
@EnableConfigurationProperties({JwtApplicationProperty.class})
public class SellingAndManagingShoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellingAndManagingShoesApplication.class, args);
    }
}
