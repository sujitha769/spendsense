package com.spendsense.transactionservice.service.impl;

import com.spendsense.transactionservice.client.AIServiceClient;
import com.spendsense.transactionservice.dto.TransactionExtractionResponse;
import com.spendsense.transactionservice.dto.TransactionRequest;
import com.spendsense.transactionservice.entity.Category;
import com.spendsense.transactionservice.entity.PaymentMethod;
import com.spendsense.transactionservice.entity.Transaction;
import com.spendsense.transactionservice.entity.TransactionType;
import com.spendsense.transactionservice.repository.TransactionRepository;
import com.spendsense.transactionservice.service.TransactionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AIServiceClient aiServiceClient;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AIServiceClient aiServiceClient) {

        this.transactionRepository = transactionRepository;
        this.aiServiceClient = aiServiceClient;
    }

    // 1. CREATE
    @Override
    public Transaction createTransaction(
            TransactionRequest request) {

        TransactionExtractionResponse extracted =
                aiServiceClient.extractTransaction(request);

        if (!extracted.isTransaction()) {
            return null;
        }

        Long userId =
                (Long) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Transaction transaction = new Transaction();

        transaction.setAmount(
                extracted.getAmount()
        );

        transaction.setType(
                TransactionType.valueOf(
                        extracted.getType()
                )
        );

        transaction.setMerchant(
                extracted.getMerchant()
        );

        transaction.setCategory(
                Category.valueOf(
                        extracted.getCategory()
                )
        );

        transaction.setPaymentMethod(
                PaymentMethod.valueOf(
                        extracted.getPaymentMethod()
                )
        );

        if (extracted.getTransactionDate() != null) {

            transaction.setTransactionDate(
                    extracted.getTransactionDate()
            );

        } else {

            transaction.setTransactionDate(
                    java.time.LocalDateTime.now()
            );
        }

        transaction.setRawMessage(
                request.getMessage()
        );

        transaction.setUserId(userId);

        return transactionRepository.save(transaction);
    }

    // 2. GET BY ID
    @Override
    public Transaction getTransactionById(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transaction not found"
                        )
                );
    }

    // 3. GET ALL
    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }

    // 4. GET MY TRANSACTIONS
    @Override
    public List<Transaction> getUserTransactions() {

        Long userId =
                (Long) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return transactionRepository.findByUserId(userId);
    }

    // 5. UPDATE
    @Override
    public Transaction updateTransaction(
            Long id,
            Transaction transaction) {

        Transaction existingTransaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        existingTransaction.setAmount(
                transaction.getAmount()
        );

        existingTransaction.setType(
                transaction.getType()
        );

        existingTransaction.setMerchant(
                transaction.getMerchant()
        );

        existingTransaction.setCategory(
                transaction.getCategory()
        );

        existingTransaction.setPaymentMethod(
                transaction.getPaymentMethod()
        );

        existingTransaction.setTransactionDate(
                transaction.getTransactionDate()
        );

        return transactionRepository.save(
                existingTransaction
        );
    }

    // 6. DELETE
    @Override
    public void deleteTransaction(Long id) {

        transactionRepository.deleteById(id);
    }
}