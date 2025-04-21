package com.example.cryptoprojectapp.repository;

import com.example.cryptoprojectapp.model.Transaction;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Find all transactions for a specific user
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    
    // Find all transactions for a specific user and cryptocurrency
    List<Transaction> findByUserAndCryptocurrencyOrderByTimestampDesc(User user, Cryptocurrency cryptocurrency);
    
    // Find all transactions of a specific type for a user
    List<Transaction> findByUserAndTypeOrderByTimestampDesc(User user, Transaction.TransactionType type);
} 