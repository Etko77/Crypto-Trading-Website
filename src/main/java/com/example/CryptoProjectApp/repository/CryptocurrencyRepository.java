package com.example.cryptoprojectapp.repository;

import com.example.cryptoprojectapp.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
    // Find cryptocurrency by symbol
    Optional<Cryptocurrency> findBySymbol(String symbol);
    
    // Check if symbol exists
    boolean existsBySymbol(String symbol);
    
    // Find top 20 cryptocurrencies by market cap
    List<Cryptocurrency> findTop20ByOrderByMarketCapDesc();
} 