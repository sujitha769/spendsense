package com.spendsense.userservice.service;

import com.spendsense.userservice.dto.LoginRequest;
import com.spendsense.userservice.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}