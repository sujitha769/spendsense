package com.spendsense.analyticsservice.service;

import com.spendsense.analyticsservice.dto.AnalyticsResponse;

public interface AnalyticsService {

    AnalyticsResponse getAnalytics(String authorizationHeader);
}