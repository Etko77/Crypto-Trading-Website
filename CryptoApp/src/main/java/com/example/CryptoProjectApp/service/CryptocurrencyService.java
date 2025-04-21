package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.dto.CoinGeckoResponse;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.repository.CryptocurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptocurrencyService {
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false";
    
    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    // Fetch and update top 20 cryptocurrencies
    public List<Cryptocurrency> updateTopCryptocurrencies() {
        CoinGeckoResponse[] response = restTemplate.getForObject(COINGECKO_API_URL, CoinGeckoResponse[].class);
        
        if (response != null) {
            return Arrays.stream(response)
                .map(this::convertToCryptocurrency)
                .map(crypto -> {
                    if (cryptocurrencyRepository.existsBySymbol(crypto.getSymbol())) {
                        Cryptocurrency existing = cryptocurrencyRepository.findBySymbol(crypto.getSymbol())
                            .orElseThrow(() -> new RuntimeException("Cryptocurrency not found"));
                        crypto.setId(existing.getId());
                    }
                    return cryptocurrencyRepository.save(crypto);
                })
                .collect(Collectors.toList());
        }
        return List.of();
    }
    
    // Convert API response to Cryptocurrency entity
    private Cryptocurrency convertToCryptocurrency(CoinGeckoResponse response) {
        Cryptocurrency crypto = new Cryptocurrency();
        crypto.setSymbol(response.getSymbol().toUpperCase());
        crypto.setName(response.getName());
        crypto.setCurrentPrice(response.getCurrent_price());
        crypto.setMarketCap(response.getMarket_cap());
        crypto.setVolume24h(response.getTotal_volume());
        crypto.setPriceChangePercentage24h(response.getPrice_change_percentage_24h());
        crypto.setImageUrl(response.getImage());
        return crypto;
    }
    
    // Get top 20 cryptocurrencies
    public List<Cryptocurrency> getTopCryptocurrencies() {
        return cryptocurrencyRepository.findTop20ByOrderByMarketCapDesc();
    }
} 