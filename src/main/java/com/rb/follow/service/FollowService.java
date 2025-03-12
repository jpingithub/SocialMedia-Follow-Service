package com.rb.follow.service;

import com.rb.follow.dto.FollowUser;
import com.rb.follow.entity.Follow;

import java.util.List;

public interface FollowService {

    Follow follow(String loggedInUser,String followingUsername);
    void unFollow(String loggedInUsername,String followingUsername);
    List<FollowUser> getFollowers(String userId);
    List<FollowUser> getFollowings(String userId);

}
