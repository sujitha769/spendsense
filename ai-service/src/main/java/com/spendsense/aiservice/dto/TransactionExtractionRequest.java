package com.spendsense.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionExtractionRequest {

    @NotBlank(message = "Transaction message cannot be empty")
    private String message;
}