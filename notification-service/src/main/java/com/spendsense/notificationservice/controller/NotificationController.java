package com.spendsense.notificationservice.controller;

import com.spendsense.notificationservice.dto.NotificationResponse;
import com.spendsense.notificationservice.service.NotificationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    @GetMapping("/roast")
    public ResponseEntity<NotificationResponse>
    generateRoast(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader) {

        return ResponseEntity.ok(
                notificationService.generateNotification(
                        authorizationHeader
                )
        );
    }
}