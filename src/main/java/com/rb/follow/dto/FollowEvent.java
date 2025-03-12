package com.rb.follow.dto;

import lombok.Data;

@Data
public class FollowEvent {
    private String toMail;
    private String content;
    private String subject;
}
