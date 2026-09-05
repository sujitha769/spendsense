package com.spendsense.analyticsservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {

    private Long id;

    private BigDecimal amount;

    private String category;

    private String merchant;

    private String paymentMethod;

    private String rawMessage;

    private LocalDateTime transactionDate;

    private String type;

    private Long userId;
}