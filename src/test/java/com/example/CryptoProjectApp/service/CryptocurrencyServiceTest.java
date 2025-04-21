package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.dto.CoinGeckoResponse;
import com.example.cryptoprojectapp.model.Cryptocurrency;
import com.example.cryptoprojectapp.repository.CryptocurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CryptocurrencyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CryptocurrencyRepository cryptocurrencyRepository;

    @InjectMocks
    private CryptocurrencyService cryptocurrencyService;

    private CoinGeckoResponse mockResponse;
    private Cryptocurrency mockCryptocurrency;

    @BeforeEach
    void setUp() {
        // Setup mock response
        mockResponse = new CoinGeckoResponse();
        mockResponse.setId("bitcoin");
        mockResponse.setSymbol("btc");
        mockResponse.setName("Bitcoin");
        mockResponse.setCurrent_price(50000.0);
        mockResponse.setMarket_cap(1000000000.0);
        mockResponse.setTotal_volume(500000000.0);
        mockResponse.setPrice_change_percentage_24h(2.5);
        mockResponse.setImage("https://example.com/bitcoin.png");

        // Setup mock cryptocurrency
        mockCryptocurrency = new Cryptocurrency();
        mockCryptocurrency.setSymbol("BTC");
        mockCryptocurrency.setName("Bitcoin");
        mockCryptocurrency.setCurrentPrice(50000.0);
        mockCryptocurrency.setMarketCap(1000000000.0);
        mockCryptocurrency.setVolume24h(500000000.0);
        mockCryptocurrency.setPriceChangePercentage24h(2.5);
        mockCryptocurrency.setImageUrl("https://example.com/bitcoin.png");

        // Setup mock repository responses
        when(cryptocurrencyRepository.existsBySymbol(anyString())).thenReturn(false);
        when(cryptocurrencyRepository.save(any(Cryptocurrency.class))).thenReturn(mockCryptocurrency);
        when(cryptocurrencyRepository.findBySymbol(anyString())).thenReturn(Optional.of(mockCryptocurrency));

        // Setup mock RestTemplate response
        when(restTemplate.getForObject(any(String.class), any())).thenReturn(new CoinGeckoResponse[]{mockResponse});
    }

    @Test
    void testUpdateTopCryptocurrencies() {
        List<Cryptocurrency> result = cryptocurrencyService.updateTopCryptocurrencies();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        Cryptocurrency crypto = result.get(0);
        assertEquals("BTC", crypto.getSymbol());
        assertEquals("Bitcoin", crypto.getName());
        assertEquals(50000.0, crypto.getCurrentPrice());
        assertEquals(1000000000.0, crypto.getMarketCap());
        assertEquals(500000000.0, crypto.getVolume24h());
        assertEquals(2.5, crypto.getPriceChangePercentage24h());
        assertEquals("https://example.com/bitcoin.png", crypto.getImageUrl());
    }
} 