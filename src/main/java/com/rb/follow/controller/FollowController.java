package com.rb.follow.controller;

import com.rb.follow.dto.FollowRequest;
import com.rb.follow.dto.FollowUser;
import com.rb.follow.entity.Follow;
import com.rb.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("following-to/{following}")
    public ResponseEntity<Follow> follow(@RequestHeader("${customized-header-for-token}")String username, @PathVariable("following") String followingUsername) {
        return new ResponseEntity<>(followService.follow(username,followingUsername), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> unFollow(@RequestBody FollowRequest followRequest) {
        followService.unFollow(followRequest);
        return ResponseEntity.ok("Un-follow successful");
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowUser>> followers(@RequestHeader("${customized-header-for-token}")String username) {
        return new ResponseEntity<>(followService.getFollowers(username), HttpStatus.OK);
    }

    @GetMapping("/followings/{id}")
    public ResponseEntity<List<FollowUser>> followings(@PathVariable("id") String userId) {
        return new ResponseEntity<>(followService.getFollowings(userId), HttpStatus.OK);
    }

}
