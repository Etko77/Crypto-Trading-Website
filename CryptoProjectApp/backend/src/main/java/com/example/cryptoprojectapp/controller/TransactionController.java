package com.example.cryptoprojectapp.controller;

import com.example.cryptoprojectapp.model.Transaction;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.service.TransactionService;
import com.example.cryptoprojectapp.service.CryptocurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.getUserTransactions(user));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<List<Transaction>> getUserTransactionsByCryptocurrency(
            @AuthenticationPrincipal User user,
            @PathVariable String symbol) {
        return cryptocurrencyService.getCryptocurrencyBySymbol(symbol)
                .map(crypto -> ResponseEntity.ok(transactionService.getUserTransactionsByCryptocurrency(user, crypto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@AuthenticationPrincipal User user, @RequestBody Transaction transaction) {
        transaction.setUser(user);
        try {
            return ResponseEntity.ok(transactionService.createTransaction(transaction));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@AuthenticationPrincipal User user, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(transactionService.getTransaction(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 