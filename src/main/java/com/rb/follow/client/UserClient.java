package com.rb.follow.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "USER-MANAGEMENT-SERVICE")
public interface UserClient {
}
