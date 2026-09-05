package com.spendsense.aiservice.controller;

import com.spendsense.aiservice.dto.TransactionExtractionRequest;
import com.spendsense.aiservice.dto.TransactionExtractionResponse;
import com.spendsense.aiservice.service.TransactionExtractionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final TransactionExtractionService transactionExtractionService;

    public AIController(
            TransactionExtractionService transactionExtractionService) {
        this.transactionExtractionService = transactionExtractionService;
    }

    @PostMapping("/extract")
    public ResponseEntity<TransactionExtractionResponse> extractTransaction(
            @Valid @RequestBody TransactionExtractionRequest request) {

        TransactionExtractionResponse response =
                transactionExtractionService.extractTransaction(request);

        return ResponseEntity.ok(response);
    }
}