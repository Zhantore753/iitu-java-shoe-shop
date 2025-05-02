package com.example.shoeshop.service;

import com.example.shoeshop.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final RestTemplate restTemplate;

    @Value("${currency.api.url:https://api.exchangerate-api.com/v4/latest/USD}")
    private String currencyApiUrl;

    @Cacheable("exchangeRates")
    public Map<String, Object> getExchangeRates() {
        try {
            log.info("Fetching exchange rates from external API (not cached)");
            ResponseEntity<Map> response = restTemplate.getForEntity(currencyApiUrl, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("rates")) {
                throw new BusinessLogicException("Invalid response from currency API");
            }

            log.info("Successfully fetched exchange rates");
            return (Map<String, Object>) responseBody.get("rates");
        } catch (Exception e) {
            log.error("Error fetching exchange rates", e);
            throw new BusinessLogicException("Failed to fetch currency exchange rates: " + e.getMessage());
        }
    }

    public BigDecimal convertPrice(BigDecimal priceInUSD, String targetCurrency) {
        if (targetCurrency == null || targetCurrency.equals("USD")) {
            return priceInUSD;
        }

        Map<String, Object> rates = getExchangeRates();

        if (!rates.containsKey(targetCurrency)) {
            log.warn("Unsupported currency: {}", targetCurrency);
            throw new BusinessLogicException("Unsupported currency: " + targetCurrency);
        }

        Double exchangeRate = ((Number) rates.get(targetCurrency)).doubleValue();
        return priceInUSD.multiply(BigDecimal.valueOf(exchangeRate)).setScale(2, RoundingMode.HALF_UP);
    }
}
