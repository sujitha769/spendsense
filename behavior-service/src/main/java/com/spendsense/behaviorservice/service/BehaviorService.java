package com.spendsense.behaviorservice.service;

import com.spendsense.behaviorservice.dto.BehaviorResponse;

public interface BehaviorService {

    BehaviorResponse analyzeBehavior(
            String authorizationHeader
    );
}