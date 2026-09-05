package com.spendsense.notificationservice.service;

import com.spendsense.notificationservice.dto.NotificationResponse;

public interface NotificationService {

    NotificationResponse generateNotification(
            String authorizationHeader
    );
}