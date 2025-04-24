package com.example.cryptoprojectapp.service;

import com.example.cryptoprojectapp.config.ApiConfig;
import com.example.cryptoprojectapp.model.KrakenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CryptoService {
    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);

    private final ApiConfig apiConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public CryptoService(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        this.restTemplate = new RestTemplate();
    }

    public List<Map<String, Object>> getTopCryptocurrencies() {
        try {
            // Get ticker information for all pairs
            String tickerUrl = apiConfig.getBaseUrl() + "/Ticker";
            logger.info("Fetching ticker data from: {}", tickerUrl);
            
            ResponseEntity<KrakenResponse> tickerResponse = restTemplate.exchange(
                tickerUrl,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                KrakenResponse.class
            );

            if (tickerResponse.getBody() == null) {
                logger.error("Empty response from Kraken API");
                throw new RuntimeException("Failed to fetch ticker information: Empty response");
            }

            if (tickerResponse.getBody().getErrors() != null && !tickerResponse.getBody().getErrors().isEmpty()) {
                logger.error("Kraken API returned errors: {}", tickerResponse.getBody().getErrors());
                throw new RuntimeException("Failed to fetch ticker information: " + 
                    String.join(", ", tickerResponse.getBody().getErrors()));
            }

            // Process and format the response
            List<Map<String, Object>> formattedResponse = new ArrayList<>();
            Map<String, KrakenResponse.TickerInfo> tickerData = tickerResponse.getBody().getResult();

            if (tickerData == null || tickerData.isEmpty()) {
                logger.error("No ticker data received from Kraken API");
                throw new RuntimeException("No ticker data available");
            }

            // Sort by volume and get top 20
            tickerData.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("USD")) // Only include USD pairs
                .sorted(Comparator.comparing(entry -> 
                    Double.parseDouble(entry.getValue().getVolume().get(0)), 
                    Comparator.reverseOrder()))
                .limit(20)
                .forEach(entry -> {
                    try {
                        KrakenResponse.TickerInfo ticker = entry.getValue();
                        Map<String, Object> cryptoData = Map.of(
                            "symbol", entry.getKey().replace("XBT", "BTC").replace("USD", ""),
                            "lastPrice", ticker.getLastTradeClosed().get(0),
                            "volume", ticker.getVolume().get(0),
                            "high", ticker.getHigh().get(0),
                            "low", ticker.getLow().get(0),
                            "change24h", calculate24hChange(ticker)
                        );
                        formattedResponse.add(cryptoData);
                    } catch (Exception e) {
                        logger.error("Error processing ticker data for {}: {}", entry.getKey(), e.getMessage());
                    }
                });

            if (formattedResponse.isEmpty()) {
                logger.error("No valid cryptocurrency data processed");
                throw new RuntimeException("No valid cryptocurrency data available");
            }

            return formattedResponse;
        } catch (Exception e) {
            logger.error("Error fetching cryptocurrencies: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch cryptocurrency data: " + e.getMessage());
        }
    }

    private String calculate24hChange(KrakenResponse.TickerInfo ticker) {
        try {
            double open = Double.parseDouble(ticker.getOpeningPrice());
            double last = Double.parseDouble(ticker.getLastTradeClosed().get(0));
            double change = ((last - open) / open) * 100;
            return String.format("%.2f%%", change);
        } catch (Exception e) {
            logger.error("Error calculating 24h change: {}", e.getMessage());
            return "N/A";
        }
    }
} 