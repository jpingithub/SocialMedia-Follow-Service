package com.rb.follow.dto;

import lombok.Data;

@Data
public class FollowRequest {
    private String followerId;
    private String followingId;
}
