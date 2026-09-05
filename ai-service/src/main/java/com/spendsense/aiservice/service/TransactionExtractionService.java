package com.spendsense.aiservice.service;

import com.spendsense.aiservice.dto.TransactionExtractionRequest;
import com.spendsense.aiservice.dto.TransactionExtractionResponse;

public interface TransactionExtractionService {

    TransactionExtractionResponse extractTransaction(
            TransactionExtractionRequest request);
}