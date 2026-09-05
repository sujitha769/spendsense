package com.spendsense.transactionservice.service;

import com.spendsense.transactionservice.dto.TransactionRequest;
import com.spendsense.transactionservice.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(TransactionRequest request);

    Transaction getTransactionById(Long id);

    List<Transaction> getAllTransactions();

    List<Transaction> getUserTransactions();

    Transaction updateTransaction(Long id, Transaction transaction);

    void deleteTransaction(Long id);
}