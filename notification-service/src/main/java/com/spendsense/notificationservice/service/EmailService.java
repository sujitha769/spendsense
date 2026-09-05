package com.spendsense.notificationservice.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String message
    );
}