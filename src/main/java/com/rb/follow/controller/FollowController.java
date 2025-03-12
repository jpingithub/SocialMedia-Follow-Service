package com.rb.follow.controller;

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
    public ResponseEntity<Follow> follow(@RequestHeader("${customized-header-for-token}")String loggedInUsername, @PathVariable("following") String followingUsername) {
        return new ResponseEntity<>(followService.follow(loggedInUsername,followingUsername), HttpStatus.CREATED);
    }

    @DeleteMapping("{followingUser}")
    public ResponseEntity<?> unFollow(@RequestHeader("${customized-header-for-token}")String loggedInUsername,@PathVariable("followingUser") String followingUsername) {
        followService.unFollow(loggedInUsername,followingUsername);
        return ResponseEntity.ok("Un-follow successful");
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowUser>> followers(@RequestHeader("${customized-header-for-token}")String username) {
        return new ResponseEntity<>(followService.getFollowers(username), HttpStatus.OK);
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<FollowUser>> followersOfOtherUser(@PathVariable("username")String username) {
        return new ResponseEntity<>(followService.getFollowers(username), HttpStatus.OK);
    }

    @GetMapping("/followings")
    public ResponseEntity<List<FollowUser>> followings(@RequestHeader("${customized-header-for-token}")String username) {
        return new ResponseEntity<>(followService.getFollowings(username), HttpStatus.OK);
    }

    @GetMapping("/followings/{username}")
    public ResponseEntity<List<FollowUser>> followingsOfOtherUser(@PathVariable("username") String username) {
        return new ResponseEntity<>(followService.getFollowings(username), HttpStatus.OK);
    }

}
