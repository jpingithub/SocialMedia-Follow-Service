package com.rb.follow.controller;

import com.rb.follow.dto.FollowRequest;
import com.rb.follow.dto.FollowUser;
import com.rb.follow.dto.User;
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

    @PostMapping
    public ResponseEntity<Follow> follow(@RequestBody FollowRequest followRequest){
        return new ResponseEntity<>(followService.follow(followRequest), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> unFollow(@RequestBody FollowRequest followRequest){
        followService.unFollow(followRequest);
        return ResponseEntity.ok("Un-follow successful");
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<List<FollowUser>> followers(@PathVariable("id") String userId){
        return new ResponseEntity<>(followService.getFollowers(userId),HttpStatus.OK);
    }

    @GetMapping("/followings/{id}")
    public ResponseEntity<List<FollowUser>> followings(@PathVariable("id") String userId){
        return new ResponseEntity<>(followService.getFollowings(userId),HttpStatus.OK);
    }

}
