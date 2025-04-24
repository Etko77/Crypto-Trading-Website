package com.example.cryptoprojectapp.controller;

import com.example.cryptoprojectapp.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {
    
    private final CryptoService cryptoService;

    @Autowired
    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/top")
    public ResponseEntity<List<Map<String, Object>>> getTopCryptocurrencies() {
        try {
            List<Map<String, Object>> cryptocurrencies = cryptoService.getTopCryptocurrencies();
            return ResponseEntity.ok(cryptocurrencies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 