package com.example.cryptoprojectapp.controller;

import com.example.cryptoprojectapp.model.Transaction;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.enums.TransactionType;
import com.example.cryptoprojectapp.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins = "http://localhost:3000")
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @PostMapping("/buy")
    public ResponseEntity<Transaction> buy(
            @AuthenticationPrincipal User user,
            @RequestParam String symbol,
            @RequestParam double quantity,
            @RequestParam double price) {
        try {
            return ResponseEntity.ok(tradeService.executeTrade(user, symbol, TransactionType.BUY, quantity, price));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<Transaction> sell(
            @AuthenticationPrincipal User user,
            @RequestParam String symbol,
            @RequestParam double quantity,
            @RequestParam double price) {
        try {
            return ResponseEntity.ok(tradeService.executeTrade(user, symbol, TransactionType.SELL, quantity, price));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
} 