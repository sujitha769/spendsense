package com.spendsense.transactionservice.client;

import com.spendsense.transactionservice.dto.TransactionExtractionResponse;
import com.spendsense.transactionservice.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AIServiceClient {

    @PostMapping("/api/ai/extract")
    TransactionExtractionResponse extractTransaction(
            @RequestBody TransactionRequest request
    );
}