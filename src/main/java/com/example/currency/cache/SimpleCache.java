package com.example.currency.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SimpleCache {
    private static final Logger logger = LoggerFactory.getLogger(SimpleCache.class);
    private final Map<String, Object> cache = new HashMap<>();

    public void put(String key, Object value) {
        logger.info("Cache put: key={}, value={}", key, value);
        cache.put(key, value);
    }

    public Optional<Object> get(String key) {
        logger.info("Cache get: key={}", key);
        Optional<Object> result = Optional.ofNullable(cache.get(key));
        if (result.isPresent()) {
            logger.info("Cache hit: key={}, value={}", key, result.get());
        } else {
            logger.info("Cache miss: key={}", key);
        }
        return result;
    }

    public void remove(String key) {
        logger.info("Cache remove: key={}", key);
        cache.remove(key);
    }

    public void clear() {
        logger.info("Cache clear: all entries removed");
        cache.clear();
    }
}