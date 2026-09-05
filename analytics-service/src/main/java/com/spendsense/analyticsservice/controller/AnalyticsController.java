package com.spendsense.analyticsservice.controller;

import com.spendsense.analyticsservice.dto.AnalyticsResponse;
import com.spendsense.analyticsservice.service.AnalyticsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService) {

        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader) {

        return ResponseEntity.ok(
                analyticsService.getAnalytics(
                        authorizationHeader
                )
        );
    }
}