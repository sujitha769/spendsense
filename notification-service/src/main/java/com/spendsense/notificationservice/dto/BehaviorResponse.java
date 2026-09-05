package com.spendsense.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BehaviorResponse {

    private String spendingPattern;

    private String dominantCategory;

    private double dominantCategoryPercentage;

    private String spendingFrequency;

    private List<String> insights;
}