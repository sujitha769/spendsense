package com.spendsense.behaviorservice.client;

import com.spendsense.behaviorservice.dto.AnalyticsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "analytics-service")
public interface AnalyticsServiceClient {

    @GetMapping("/api/analytics")
    AnalyticsResponse getAnalytics(
            @RequestHeader("Authorization")
            String authorizationHeader
    );
}