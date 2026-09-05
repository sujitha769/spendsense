package com.spendsense.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;

    private Long userId;

    private String name;

    private String email;
}