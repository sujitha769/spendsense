package com.spendsense.notificationservice.client;

import com.spendsense.notificationservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUser(
            @PathVariable("id") Long userId,
            @RequestHeader("Authorization")
            String authorizationHeader
    );
}