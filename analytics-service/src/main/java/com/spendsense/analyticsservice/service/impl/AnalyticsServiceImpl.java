package com.spendsense.analyticsservice.service.impl;

import com.spendsense.analyticsservice.client.TransactionServiceClient;
import com.spendsense.analyticsservice.dto.AnalyticsResponse;
import com.spendsense.analyticsservice.dto.TransactionResponse;
import com.spendsense.analyticsservice.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TransactionServiceClient transactionServiceClient;

    public AnalyticsServiceImpl(
            TransactionServiceClient transactionServiceClient) {

        this.transactionServiceClient = transactionServiceClient;
    }

    @Override
    public AnalyticsResponse getAnalytics(
            String authorizationHeader) {

        List<TransactionResponse> transactions =
                transactionServiceClient.getUserTransactions(
                        authorizationHeader
                );

        BigDecimal totalExpense = BigDecimal.ZERO;

        Map<String, BigDecimal> categoryWiseExpenses =
                new HashMap<>();

        int totalTransactions = 0;

        for (TransactionResponse transaction : transactions) {

            if ("EXPENSE".equals(transaction.getType())) {

                totalTransactions++;

                BigDecimal amount =
                        transaction.getAmount();

                totalExpense =
                        totalExpense.add(amount);

                String category =
                        transaction.getCategory();

                categoryWiseExpenses.merge(
                        category,
                        amount,
                        BigDecimal::add
                );
            }
        }

        String topCategory = null;
        BigDecimal topCategoryAmount = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> entry :
                categoryWiseExpenses.entrySet()) {

            if (entry.getValue()
                    .compareTo(topCategoryAmount) > 0) {

                topCategory = entry.getKey();
                topCategoryAmount = entry.getValue();
            }
        }

        BigDecimal averageExpense = BigDecimal.ZERO;

        if (totalTransactions > 0) {

            averageExpense =
                    totalExpense.divide(
                            BigDecimal.valueOf(
                                    totalTransactions
                            ),
                            2,
                            java.math.RoundingMode.HALF_UP
                    );
        }

        AnalyticsResponse response =
                new AnalyticsResponse();

        response.setTotalExpense(totalExpense);

        response.setCategoryWiseExpenses(
                categoryWiseExpenses
        );

        response.setTopCategory(topCategory);

        response.setTopCategoryAmount(
                topCategoryAmount
        );

        response.setTotalTransactions(
                totalTransactions
        );

        response.setAverageExpense(
                averageExpense
        );

        return response;
    }
}