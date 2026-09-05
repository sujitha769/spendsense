package com.spendsense.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {

    private boolean shouldNotify;

    private String alertType;

    private String message;
}