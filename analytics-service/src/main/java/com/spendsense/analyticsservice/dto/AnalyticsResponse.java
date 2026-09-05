package com.spendsense.analyticsservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class AnalyticsResponse {

    private BigDecimal totalExpense;

    private Map<String, BigDecimal> categoryWiseExpenses;

    private String topCategory;

    private BigDecimal topCategoryAmount;

    private int totalTransactions;

    private BigDecimal averageExpense;
}