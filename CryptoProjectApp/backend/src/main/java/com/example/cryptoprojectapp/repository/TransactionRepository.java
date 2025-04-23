package com.example.cryptoprojectapp.repository;

import com.example.cryptoprojectapp.model.Transaction;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    List<Transaction> findByUserAndCryptocurrencyOrderByTimestampDesc(User user, Cryptocurrency cryptocurrency);
} 