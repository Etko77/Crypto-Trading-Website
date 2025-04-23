package com.example.cryptoprojectapp.repository;

import com.example.cryptoprojectapp.model.Portfolio;
import com.example.cryptoprojectapp.model.User;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUser(User user);
    Optional<Portfolio> findByUserAndCryptocurrency(User user, Cryptocurrency cryptocurrency);
    boolean existsByUserAndCryptocurrency(User user, Cryptocurrency cryptocurrency);
} 