package com.spendsense.analyticsservice.client;

import com.spendsense.analyticsservice.dto.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "transaction-service")
public interface TransactionServiceClient {

    @GetMapping("/api/transactions/my")
    List<TransactionResponse> getUserTransactions(
            @RequestHeader("Authorization")
            String authorizationHeader
    );
}