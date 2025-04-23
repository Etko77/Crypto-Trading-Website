package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.model.Portfolio;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<Portfolio> getUserPortfolio(User user) {
        return portfolioRepository.findByUser(user);
    }

    public Optional<Portfolio> getUserPortfolioItem(User user, Cryptocurrency cryptocurrency) {
        return portfolioRepository.findByUserAndCryptocurrency(user, cryptocurrency);
    }

    public Portfolio addToPortfolio(@Valid Portfolio portfolio) {
        if (portfolioRepository.existsByUserAndCryptocurrency(portfolio.getUser(), portfolio.getCryptocurrency())) {
            throw new IllegalArgumentException("Portfolio item already exists for this user and cryptocurrency");
        }
        return portfolioRepository.save(portfolio);
    }

    public Portfolio updatePortfolio(@Valid Portfolio portfolio) {
        if (!portfolioRepository.existsById(portfolio.getId())) {
            throw new IllegalArgumentException("Portfolio item with id " + portfolio.getId() + " does not exist");
        }
        return portfolioRepository.save(portfolio);
    }

    public void removeFromPortfolio(Long id) {
        if (!portfolioRepository.existsById(id)) {
            throw new IllegalArgumentException("Portfolio item with id " + id + " does not exist");
        }
        portfolioRepository.deleteById(id);
    }
} 