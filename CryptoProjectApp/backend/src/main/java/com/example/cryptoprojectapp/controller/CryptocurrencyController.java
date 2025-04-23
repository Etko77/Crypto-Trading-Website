package com.example.cryptoprojectapp.controller;

import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.service.CryptocurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cryptocurrencies")
@CrossOrigin(origins = "http://localhost:3000")
public class CryptocurrencyController {
    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @GetMapping
    public ResponseEntity<List<Cryptocurrency>> getAllCryptocurrencies() {
        return ResponseEntity.ok(cryptocurrencyService.getAllCryptocurrencies());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Cryptocurrency> getCryptocurrencyBySymbol(@PathVariable String symbol) {
        return cryptocurrencyService.getCryptocurrencyBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cryptocurrency> createCryptocurrency(@RequestBody Cryptocurrency cryptocurrency) {
        try {
            return ResponseEntity.ok(cryptocurrencyService.createCryptocurrency(cryptocurrency));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cryptocurrency> updateCryptocurrency(@PathVariable Long id, @RequestBody Cryptocurrency cryptocurrency) {
        cryptocurrency.setId(id);
        try {
            return ResponseEntity.ok(cryptocurrencyService.updateCryptocurrency(cryptocurrency));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCryptocurrency(@PathVariable Long id) {
        try {
            cryptocurrencyService.deleteCryptocurrency(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 