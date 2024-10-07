package com.taidev198;

import com.taidev198.config.JwtApplicationProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtApplicationProperty.class})
public class SellingAndManagingShoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellingAndManagingShoesApplication.class, args);
    }
}
