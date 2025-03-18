package com.rb.follow.client;

import com.rb.follow.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-MANAGEMENT-SERVICE")
public interface UserClient {
    @GetMapping("/api/v1/users/search")
    ResponseEntity<User> searchUser(@RequestParam("username") String username);
}
