package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.model.*;
import com.example.cryptoprojectapp.enums.TransactionType;
import com.example.cryptoprojectapp.repository.PortfolioRepository;
import com.example.cryptoprojectapp.repository.TransactionRepository;
import com.example.cryptoprojectapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TradeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @Transactional
    public Transaction executeTrade(User user, String symbol, TransactionType type, double quantity, double price) {
        // Validate user balance for buy trades
        if (type == TransactionType.BUY) {
            double totalCost = quantity * price;
            if (user.getBalance() < totalCost) {
                throw new IllegalArgumentException("Insufficient balance");
            }
        }

        // Get cryptocurrency
        Cryptocurrency cryptocurrency = cryptocurrencyService.getCryptocurrencyBySymbol(symbol)
                .orElseThrow(() -> new IllegalArgumentException("Cryptocurrency not found"));

        // Validate sell trades
        if (type == TransactionType.SELL) {
            Portfolio portfolio = portfolioRepository.findByUserAndCryptocurrency(user, cryptocurrency)
                    .orElseThrow(() -> new IllegalArgumentException("No holdings found for this cryptocurrency"));
            if (portfolio.getQuantity() < quantity) {
                throw new IllegalArgumentException("Insufficient cryptocurrency holdings");
            }
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCryptocurrency(cryptocurrency);
        transaction.setType(type);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(quantity * price);

        // Update user balance
        if (type == TransactionType.BUY) {
            user.setBalance(user.getBalance() - transaction.getTotalAmount());
        } else {
            user.setBalance(user.getBalance() + transaction.getTotalAmount());
        }
        userRepository.save(user);

        // Update portfolio
        updatePortfolio(user, cryptocurrency, type, quantity, price);

        // Save transaction
        return transactionRepository.save(transaction);
    }

    private void updatePortfolio(User user, Cryptocurrency cryptocurrency, TransactionType type, double quantity, double price) {
        Portfolio portfolio = portfolioRepository.findByUserAndCryptocurrency(user, cryptocurrency)
                .orElse(new Portfolio());

        if (portfolio.getId() == null) {
            portfolio.setUser(user);
            portfolio.setCryptocurrency(cryptocurrency);
            portfolio.setQuantity(0.0);
            portfolio.setAverageBuyPrice(0.0);
        }

        if (type == TransactionType.BUY) {
            double newQuantity = portfolio.getQuantity() + quantity;
            double newAveragePrice = ((portfolio.getQuantity() * portfolio.getAverageBuyPrice()) + (quantity * price)) / newQuantity;
            portfolio.setQuantity(newQuantity);
            portfolio.setAverageBuyPrice(newAveragePrice);
        } else {
            double newQuantity = portfolio.getQuantity() - quantity;
            if (newQuantity == 0) {
                portfolioRepository.delete(portfolio);
                return;
            }
            portfolio.setQuantity(newQuantity);
        }

        portfolioRepository.save(portfolio);
    }
} 