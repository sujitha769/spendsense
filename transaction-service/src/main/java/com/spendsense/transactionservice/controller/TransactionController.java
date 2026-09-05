package com.spendsense.transactionservice.controller;

import com.spendsense.transactionservice.dto.TransactionRequest;
import com.spendsense.transactionservice.entity.Transaction;
import com.spendsense.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    // 1. CREATE
    @PostMapping
    public ResponseEntity<?> createTransaction(
            @Valid @RequestBody TransactionRequest request) {

        Transaction savedTransaction =
                transactionService.createTransaction(
                        request
                );

        if (savedTransaction == null) {

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Not a financial transaction. Ignored."
                    )
            );
        }

        return ResponseEntity.ok(savedTransaction);
    }

    // 2. GET MY TRANSACTIONS
    @GetMapping("/my")
    public ResponseEntity<List<Transaction>>
    getMyTransactions() {

        return ResponseEntity.ok(
                transactionService.getUserTransactions()
        );
    }

    // 3. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction>
    getTransactionById(
            @PathVariable Long id) {

        Transaction transaction =
                transactionService.getTransactionById(id);

        return ResponseEntity.ok(transaction);
    }

    // 4. GET ALL
    @GetMapping
    public ResponseEntity<List<Transaction>>
    getAllTransactions() {

        List<Transaction> transactions =
                transactionService.getAllTransactions();

        return ResponseEntity.ok(transactions);
    }

    // 5. UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Transaction>
    updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction transaction) {

        Transaction updatedTransaction =
                transactionService.updateTransaction(
                        id,
                        transaction
                );

        return ResponseEntity.ok(
                updatedTransaction
        );
    }

    // 6. DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteTransaction(
            @PathVariable Long id) {

        transactionService.deleteTransaction(id);

        return ResponseEntity.noContent().build();
    }
}