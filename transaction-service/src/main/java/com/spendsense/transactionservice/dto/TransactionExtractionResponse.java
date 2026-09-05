package com.spendsense.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionExtractionResponse {

    @JsonProperty("isTransaction")
    private boolean transaction;

    private BigDecimal amount;

    private String type;

    private String merchant;

    private String category;

    private String paymentMethod;

    private LocalDateTime transactionDate;
}