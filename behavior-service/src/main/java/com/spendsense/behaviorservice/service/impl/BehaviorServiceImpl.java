package com.spendsense.behaviorservice.service.impl;

import com.spendsense.behaviorservice.client.AnalyticsServiceClient;
import com.spendsense.behaviorservice.dto.AnalyticsResponse;
import com.spendsense.behaviorservice.dto.BehaviorResponse;
import com.spendsense.behaviorservice.service.BehaviorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BehaviorServiceImpl implements BehaviorService {

    private final AnalyticsServiceClient analyticsServiceClient;

    public BehaviorServiceImpl(
            AnalyticsServiceClient analyticsServiceClient) {

        this.analyticsServiceClient =
                analyticsServiceClient;
    }

    @Override
    public BehaviorResponse analyzeBehavior(
            String authorizationHeader) {

        AnalyticsResponse analytics =
                analyticsServiceClient.getAnalytics(
                        authorizationHeader
                );

        BehaviorResponse response =
                new BehaviorResponse();

        List<String> insights =
                new ArrayList<>();

        BigDecimal totalExpense =
                analytics.getTotalExpense();

        String topCategory =
                analytics.getTopCategory();

        BigDecimal topCategoryAmount =
                analytics.getTopCategoryAmount();

        int totalTransactions =
                analytics.getTotalTransactions();

        /*
         * Calculate dominant category percentage
         */
        double categoryPercentage = 0.0;

        if (totalExpense != null
                && totalExpense.compareTo(BigDecimal.ZERO) > 0
                && topCategoryAmount != null) {

            categoryPercentage =
                    topCategoryAmount
                            .divide(
                                    totalExpense,
                                    4,
                                    java.math.RoundingMode.HALF_UP
                            )
                            .doubleValue()
                            * 100;
        }

        /*
         * Determine spending pattern
         */
        String spendingPattern;

        if (categoryPercentage >= 70) {

            spendingPattern =
                    "Highly concentrated spending";

            insights.add(
                    "Most of your spending is concentrated in "
                            + topCategory
                            + "."
            );

        } else if (categoryPercentage >= 40) {

            spendingPattern =
                    "Moderately concentrated spending";

            insights.add(
                    topCategory
                            + " is your dominant spending category."
            );

        } else {

            spendingPattern =
                    "Diversified spending";

            insights.add(
                    "Your spending is spread across multiple categories."
            );
        }

        /*
         * Dominant category insight
         */
        if (topCategory != null) {

            insights.add(
                    String.format(
                            "%.2f%% of your spending is in %s.",
                            categoryPercentage,
                            topCategory
                    )
            );
        }

        /*
         * Spending frequency
         */
        String spendingFrequency;

        if (totalTransactions <= 3) {

            spendingFrequency =
                    "Low transaction frequency";

        } else if (totalTransactions <= 10) {

            spendingFrequency =
                    "Moderate transaction frequency";

        } else {

            spendingFrequency =
                    "High transaction frequency";

        }

        /*
         * Average expense insight
         */
        if (analytics.getAverageExpense() != null
                && analytics.getAverageExpense()
                .compareTo(BigDecimal.ZERO) > 0) {

            insights.add(
                    "Your average expense is ₹"
                            + analytics
                            .getAverageExpense()
                            .toPlainString()
                            + "."
            );
        }

        /*
         * Build response
         */
        response.setSpendingPattern(
                spendingPattern
        );

        response.setDominantCategory(
                topCategory
        );

        response.setDominantCategoryPercentage(
                Math.round(categoryPercentage * 100.0)
                        / 100.0
        );

        response.setSpendingFrequency(
                spendingFrequency
        );

        response.setInsights(
                insights
        );

        return response;
    }
}