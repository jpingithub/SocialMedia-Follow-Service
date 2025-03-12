package com.rb.follow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SocialMediaFollowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaFollowServiceApplication.class, args);
    }

}
