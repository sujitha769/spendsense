package com.spendsense.notificationservice.client;

import com.spendsense.notificationservice.dto.BehaviorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "behavior-service")
public interface BehaviorServiceClient {

    @GetMapping("/api/behavior")
    BehaviorResponse getBehavior(
            @RequestHeader("Authorization")
            String authorizationHeader
    );
}