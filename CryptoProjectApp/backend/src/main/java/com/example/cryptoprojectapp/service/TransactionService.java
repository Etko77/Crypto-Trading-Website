package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.model.Transaction;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;

@Service
@Validated
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByUserOrderByTimestampDesc(user);
    }

    public List<Transaction> getUserTransactionsByCryptocurrency(User user, Cryptocurrency cryptocurrency) {
        return transactionRepository.findByUserAndCryptocurrencyOrderByTimestampDesc(user, cryptocurrency);
    }

    public Transaction createTransaction(@Valid Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction with id " + id + " does not exist"));
    }
} 