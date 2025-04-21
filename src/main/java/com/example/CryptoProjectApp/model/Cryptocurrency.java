package com.example.cryptoprojectapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cryptocurrencies")
@Data
public class Cryptocurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String symbol;

    private String name;
    private double currentPrice;
    private double marketCap;
    private double volume24h;
    private double priceChangePercentage24h;
    private String imageUrl;
} 