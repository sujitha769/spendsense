package com.spendsense.userservice.service;

import com.spendsense.userservice.dto.UserRegistrationRequest;
import com.spendsense.userservice.dto.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRegistrationRequest request);

    UserResponse getUserById(Long id);
}