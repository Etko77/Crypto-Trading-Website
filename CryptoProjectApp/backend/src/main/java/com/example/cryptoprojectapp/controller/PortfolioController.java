package com.example.cryptoprojectapp.controller;

import com.example.cryptoprojectapp.model.Portfolio;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.service.PortfolioService;
import com.example.cryptoprojectapp.service.CryptocurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @GetMapping
    public ResponseEntity<List<Portfolio>> getUserPortfolio(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(portfolioService.getUserPortfolio(user));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Portfolio> getUserPortfolioItem(@AuthenticationPrincipal User user, @PathVariable String symbol) {
        return cryptocurrencyService.getCryptocurrencyBySymbol(symbol)
                .flatMap(crypto -> portfolioService.getUserPortfolioItem(user, crypto))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Portfolio> addToPortfolio(@AuthenticationPrincipal User user, @RequestBody Portfolio portfolio) {
        portfolio.setUser(user);
        try {
            return ResponseEntity.ok(portfolioService.addToPortfolio(portfolio));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Portfolio portfolio) {
        portfolio.setId(id);
        portfolio.setUser(user);
        try {
            return ResponseEntity.ok(portfolioService.updatePortfolio(portfolio));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromPortfolio(@AuthenticationPrincipal User user, @PathVariable Long id) {
        try {
            portfolioService.removeFromPortfolio(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 