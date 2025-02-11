package com.rb.follow.dto;

import lombok.Data;

@Data
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Boolean isEmailVerified;
}
