package com.example.currency.client;

import com.example.currency.models.CurrencyInfo;
import com.example.currency.models.CurrencyRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class NbrbApiClient {
    private static final Logger logger = LoggerFactory.getLogger(NbrbApiClient.class);
    private static final String API_BASE_URL = "https://api.nbrb.by/exrates/";
    private final RestTemplate restTemplate;

    public NbrbApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<CurrencyInfo> getAllCurrencies() {
        String url = API_BASE_URL + "currencies";
        try {
            logger.info("Fetching all currencies from URL: {}", url);
            ResponseEntity<CurrencyInfo[]> response = restTemplate.getForEntity(url, CurrencyInfo[].class);
            List<CurrencyInfo> currencies = Arrays.asList(Objects.requireNonNull(response.getBody()));
            logger.info("Successfully fetched {} currencies", currencies.size());
            return currencies;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("Client error fetching currencies: {}", e.getMessage());
                throw new CurrencyNotFoundException("Client error: " + e.getMessage(), e);
            }
            throw e;
        } catch (HttpServerErrorException e) {
            logger.error("Server error fetching currencies: {}", e.getMessage());
            throw new RuntimeException("Server error: " + e.getMessage(), e);
        }
    }

    public CurrencyRate getCurrencyRate(Integer currencyId) {
        String url = API_BASE_URL + "rates/" + currencyId;
        try {
            logger.info("Fetching rate for currency ID: {} from URL: {}", currencyId, url);
            CurrencyRate rate = restTemplate.getForObject(url, CurrencyRate.class);
            logger.info("Successfully fetched rate for currency ID: {}", currencyId);
            return rate;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("Client error fetching rate for currency ID {}: {}", currencyId, e.getMessage());
                throw new CurrencyNotFoundException("Currency rate not found for ID: " + currencyId, e);
            }
            throw e;
        } catch (HttpServerErrorException e) {
            logger.error("Server error fetching rate for currency ID {}: {}", currencyId, e.getMessage());
            throw new RuntimeException("Server error: " + e.getMessage(), e);
        }
    }
}

class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
