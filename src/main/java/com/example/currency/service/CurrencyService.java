package com.example.currency.service;

import com.example.currency.cache.SimpleCache;
import com.example.currency.client.NbrbApiClient;
import com.example.currency.models.CurrencyInfo;
import com.example.currency.models.CurrencyRate;
import com.example.currency.repository.CurrencyInfoRepository;
import com.example.currency.repository.CurrencyRateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private final CurrencyInfoRepository currencyInfoRepository;
    private final CurrencyRateRepository currencyRateRepository;
    private final NbrbApiClient apiClient;
    private final SimpleCache cacheService;

    public CurrencyService(
            CurrencyInfoRepository currencyInfoRepository,
            CurrencyRateRepository currencyRateRepository,
            NbrbApiClient apiClient,
            SimpleCache cacheService) {
        this.currencyInfoRepository = currencyInfoRepository;
        this.currencyRateRepository = currencyRateRepository;
        this.apiClient = apiClient;
        this.cacheService = cacheService;
    }

    public List<CurrencyInfo> getAllCurrencies() {
        String cacheKey = "allCurrencies";
        Optional<Object> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            return (List<CurrencyInfo>) cached.get();
        }

        List<CurrencyInfo> dbCurrencies = currencyInfoRepository.findAll();
        if (dbCurrencies.isEmpty()) {
            List<CurrencyInfo> apiCurrencies = apiClient.getAllCurrencies();
            dbCurrencies = currencyInfoRepository.saveAll(apiCurrencies);
        }
        cacheService.put(cacheKey, dbCurrencies);
        return dbCurrencies;
    }

    public CurrencyRate getCurrencyRate(Integer currencyId) {
        String cacheKey = "rate:" + currencyId + ":" + LocalDate.now();
        Optional<Object> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            return ((List<CurrencyRate>) cached.get()).get(0);
        }

        Optional<CurrencyInfo> currency = currencyInfoRepository.findById(currencyId);
        if (currency.isPresent()) {
            List<CurrencyRate> rates = currency.get().getRates();
            Optional<CurrencyRate> latestRate = rates.stream()
                    .filter(rate -> rate.getDate().equals(LocalDate.now()))
                    .findFirst();
            if (latestRate.isPresent()) {
                cacheService.put(cacheKey, List.of(latestRate.get()));
                return latestRate.get();
            }
        }

        CurrencyRate rate = apiClient.getCurrencyRate(currencyId);
        if (currency.isPresent()) {
            rate.setCurrency(currency.get());
            currencyRateRepository.save(rate);
            cacheService.put(cacheKey, List.of(rate));
        }
        return rate;
    }

    public CurrencyInfo createCurrency(CurrencyInfo currencyInfo) {
        CurrencyInfo saved = currencyInfoRepository.save(currencyInfo);
        cacheService.clear();
        return saved;
    }

    public List<CurrencyInfo> getAllCurrenciesFromDb() {
        String cacheKey = "allCurrenciesFromDb";
        Optional<Object> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            return (List<CurrencyInfo>) cached.get();
        }

        List<CurrencyInfo> currencies = currencyInfoRepository.findAll();
        cacheService.put(cacheKey, currencies);
        return currencies;
    }

    public Optional<CurrencyInfo> getCurrencyById(Integer id) {
        String cacheKey = "currency:" + id;
        Optional<Object> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            return Optional.of((CurrencyInfo) cached.get());
        }

        Optional<CurrencyInfo> currency = currencyInfoRepository.findById(id);
        currency.ifPresent(c -> cacheService.put(cacheKey, c));
        return currency;
    }

    public CurrencyInfo updateCurrency(Integer id, CurrencyInfo updatedCurrency) {
        Optional<CurrencyInfo> existingCurrency = currencyInfoRepository.findById(id);
        if (existingCurrency.isPresent()) {
            CurrencyInfo currency = existingCurrency.get();
            currency.setCode(updatedCurrency.getCode());
            currency.setAbbreviation(updatedCurrency.getAbbreviation());
            currency.setName(updatedCurrency.getName());
            currency.setScale(updatedCurrency.getScale());
            CurrencyInfo saved = currencyInfoRepository.save(currency);
            cacheService.clear();
            return saved;
        }
        throw new RuntimeException("Currency not found with id: " + id);
    }

    public void deleteCurrency(Integer id) {
        currencyInfoRepository.deleteById(id);
        cacheService.clear();
    }
}