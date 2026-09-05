package com.spendsense.behaviorservice.controller;

import com.spendsense.behaviorservice.dto.BehaviorResponse;
import com.spendsense.behaviorservice.service.BehaviorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/behavior")
public class BehaviorController {

    private final BehaviorService behaviorService;

    public BehaviorController(
            BehaviorService behaviorService) {

        this.behaviorService =
                behaviorService;
    }

    @GetMapping
    public ResponseEntity<BehaviorResponse> analyzeBehavior(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader) {

        return ResponseEntity.ok(
                behaviorService.analyzeBehavior(
                        authorizationHeader
                )
        );
    }
}