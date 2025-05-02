package com.example.shoeshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShoeShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoeShopApplication.class, args);
    }

}
