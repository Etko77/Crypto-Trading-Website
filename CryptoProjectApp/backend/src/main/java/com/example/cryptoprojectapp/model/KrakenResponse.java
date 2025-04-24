package com.example.cryptoprojectapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class KrakenResponse {
    @JsonProperty("error")
    private List<String> errors;
    
    @JsonProperty("result")
    private Map<String, TickerInfo> result;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Map<String, TickerInfo> getResult() {
        return result;
    }

    public void setResult(Map<String, TickerInfo> result) {
        this.result = result;
    }

    public static class TickerInfo {
        @JsonProperty("a")
        private List<String> ask;
        
        @JsonProperty("b")
        private List<String> bid;
        
        @JsonProperty("c")
        private List<String> lastTradeClosed;
        
        @JsonProperty("v")
        private List<String> volume;
        
        @JsonProperty("p")
        private List<String> volumeWeightedAveragePrice;
        
        @JsonProperty("t")
        private List<Integer> numberOfTrades;
        
        @JsonProperty("l")
        private List<String> low;
        
        @JsonProperty("h")
        private List<String> high;
        
        @JsonProperty("o")
        private String openingPrice;

        // Getters and Setters
        public List<String> getAsk() {
            return ask;
        }

        public void setAsk(List<String> ask) {
            this.ask = ask;
        }

        public List<String> getBid() {
            return bid;
        }

        public void setBid(List<String> bid) {
            this.bid = bid;
        }

        public List<String> getLastTradeClosed() {
            return lastTradeClosed;
        }

        public void setLastTradeClosed(List<String> lastTradeClosed) {
            this.lastTradeClosed = lastTradeClosed;
        }

        public List<String> getVolume() {
            return volume;
        }

        public void setVolume(List<String> volume) {
            this.volume = volume;
        }

        public List<String> getVolumeWeightedAveragePrice() {
            return volumeWeightedAveragePrice;
        }

        public void setVolumeWeightedAveragePrice(List<String> volumeWeightedAveragePrice) {
            this.volumeWeightedAveragePrice = volumeWeightedAveragePrice;
        }

        public List<Integer> getNumberOfTrades() {
            return numberOfTrades;
        }

        public void setNumberOfTrades(List<Integer> numberOfTrades) {
            this.numberOfTrades = numberOfTrades;
        }

        public List<String> getLow() {
            return low;
        }

        public void setLow(List<String> low) {
            this.low = low;
        }

        public List<String> getHigh() {
            return high;
        }

        public void setHigh(List<String> high) {
            this.high = high;
        }

        public String getOpeningPrice() {
            return openingPrice;
        }

        public void setOpeningPrice(String openingPrice) {
            this.openingPrice = openingPrice;
        }
    }
} 