package com.example.cryptoprojectapp.dto;

import lombok.Data;

@Data
public class CoinGeckoResponse {
    private String id;
    private String symbol;
    private String name;
    private double current_price;
    private double market_cap;
    private double total_volume;
    private double price_change_percentage_24h;
    private String image;
} 