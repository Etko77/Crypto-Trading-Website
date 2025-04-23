package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.repository.CryptocurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class CryptocurrencyService {
    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    public List<Cryptocurrency> getAllCryptocurrencies() {
        return cryptocurrencyRepository.findAll();
    }

    public Optional<Cryptocurrency> getCryptocurrencyBySymbol(String symbol) {
        return cryptocurrencyRepository.findBySymbol(symbol);
    }

    public Cryptocurrency createCryptocurrency(@Valid Cryptocurrency cryptocurrency) {
        if (cryptocurrencyRepository.existsBySymbol(cryptocurrency.getSymbol())) {
            throw new IllegalArgumentException("Cryptocurrency with symbol " + cryptocurrency.getSymbol() + " already exists");
        }
        return cryptocurrencyRepository.save(cryptocurrency);
    }

    public Cryptocurrency updateCryptocurrency(@Valid Cryptocurrency cryptocurrency) {
        if (!cryptocurrencyRepository.existsById(cryptocurrency.getId())) {
            throw new IllegalArgumentException("Cryptocurrency with id " + cryptocurrency.getId() + " does not exist");
        }
        return cryptocurrencyRepository.save(cryptocurrency);
    }

    public void deleteCryptocurrency(Long id) {
        if (!cryptocurrencyRepository.existsById(id)) {
            throw new IllegalArgumentException("Cryptocurrency with id " + id + " does not exist");
        }
        cryptocurrencyRepository.deleteById(id);
    }
} 