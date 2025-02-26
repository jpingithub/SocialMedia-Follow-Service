package com.rb.follow.service;

import com.rb.follow.dto.FollowRequest;
import com.rb.follow.dto.FollowUser;
import com.rb.follow.dto.User;
import com.rb.follow.entity.Follow;

import java.util.List;

public interface FollowService {

    Follow follow(String loggedInUser,String followingUsername);
    void unFollow(FollowRequest followRequest);
    List<FollowUser> getFollowers(String userId);
    List<FollowUser> getFollowings(String userId);

}
