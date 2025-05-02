package com.example.shoeshop.service;

import com.example.shoeshop.dto.ShoeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service to handle Redis caching of DTOs directly, avoiding JPA entity serialization issues
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DTOCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final long CACHE_TTL_MINUTES = 60; // 1 hour cache TTL

    // Cache keys
    private static final String SHOE_DTO_BY_ID_KEY = "shoe:dto:id:%d:%s";   // id, currency
    private static final String ALL_SHOES_DTO_KEY = "shoes:dto:all:%s";      // currency
    private static final String SEARCH_SHOES_DTO_KEY = "shoes:dto:search:%s:%s";  // query, currency
    private static final String FILTER_SHOES_DTO_KEY = "shoes:dto:filter:%s:%s:%s:%s:%s:%s:%s"; // name, brand, minPrice, maxPrice, categoryId, companyId, currency

    /**
     * Get a ShoeDto from cache by id and currency
     *
     * @param id Shoe ID
     * @param currency Currency code
     * @return Cached ShoeDto or null if not found
     */
    public ShoeDto getShoeDto(Long id, String currency) {
        String key = String.format(SHOE_DTO_BY_ID_KEY, id, currency);
        try {
            Object cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue != null && cachedValue instanceof ShoeDto) {
                log.debug("Cache hit for shoe ID: {} with currency: {}", id, currency);
                return (ShoeDto) cachedValue;
            }
            log.debug("Cache miss for shoe ID: {} with currency: {}", id, currency);
            return null;
        } catch (Exception e) {
            log.error("Error retrieving shoe DTO from cache: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Store a ShoeDto in cache
     *
     * @param id Shoe ID
     * @param currency Currency code
     * @param shoeDto ShoeDto to cache
     */
    public void cacheShoeDto(Long id, String currency, ShoeDto shoeDto) {
        String key = String.format(SHOE_DTO_BY_ID_KEY, id, currency);
        try {
            redisTemplate.opsForValue().set(key, shoeDto, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.debug("Cached shoe DTO with ID: {} and currency: {}", id, currency);
        } catch (Exception e) {
            log.error("Error caching shoe DTO: {}", e.getMessage());
        }
    }

    /**
     * Get all shoes DTOs from cache for a specific currency
     *
     * @param currency Currency code
     * @return List of cached ShoeDtos or null if not found
     */
    @SuppressWarnings("unchecked")
    public List<ShoeDto> getAllShoeDtos(String currency) {
        String key = String.format(ALL_SHOES_DTO_KEY, currency);
        try {
            Object cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue != null && cachedValue instanceof List<?>) {
                log.debug("Cache hit for all shoes with currency: {}", currency);
                return (List<ShoeDto>) cachedValue;
            }
            log.debug("Cache miss for all shoes with currency: {}", currency);
            return null;
        } catch (Exception e) {
            log.error("Error retrieving all shoe DTOs from cache: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Store all shoes DTOs in cache for a specific currency
     *
     * @param currency Currency code
     * @param shoeDtos List of ShoeDtos to cache
     */
    public void cacheAllShoeDtos(String currency, List<ShoeDto> shoeDtos) {
        String key = String.format(ALL_SHOES_DTO_KEY, currency);
        try {
            redisTemplate.opsForValue().set(key, new ArrayList<>(shoeDtos), CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.debug("Cached all shoes DTOs with currency: {}", currency);
        } catch (Exception e) {
            log.error("Error caching all shoe DTOs: {}", e.getMessage());
        }
    }

    /**
     * Get search results from cache
     *
     * @param query Search query
     * @param currency Currency code
     * @return List of cached ShoeDtos or null if not found
     */
    @SuppressWarnings("unchecked")
    public List<ShoeDto> getSearchResults(String query, String currency) {
        String key = String.format(SEARCH_SHOES_DTO_KEY, query, currency);
        try {
            Object cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue != null && cachedValue instanceof List<?>) {
                log.debug("Cache hit for search query: '{}' with currency: {}", query, currency);
                return (List<ShoeDto>) cachedValue;
            }
            log.debug("Cache miss for search query: '{}' with currency: {}", query, currency);
            return null;
        } catch (Exception e) {
            log.error("Error retrieving search results from cache: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Store search results in cache
     *
     * @param query Search query
     * @param currency Currency code
     * @param shoeDtos List of ShoeDtos to cache
     */
    public void cacheSearchResults(String query, String currency, List<ShoeDto> shoeDtos) {
        String key = String.format(SEARCH_SHOES_DTO_KEY, query, currency);
        try {
            redisTemplate.opsForValue().set(key, new ArrayList<>(shoeDtos), CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.debug("Cached search results for query: '{}' with currency: {}", query, currency);
        } catch (Exception e) {
            log.error("Error caching search results: {}", e.getMessage());
        }
    }

    /**
     * Get filter results from cache
     *
     * @param name Filter by name
     * @param brand Filter by brand
     * @param minPrice Filter by min price
     * @param maxPrice Filter by max price
     * @param categoryId Filter by category ID
     * @param companyId Filter by company ID
     * @param currency Currency code
     * @return List of cached ShoeDtos or null if not found
     */
    @SuppressWarnings("unchecked")
    public List<ShoeDto> getFilterResults(String name, String brand, String minPrice,
                                          String maxPrice, String categoryId,
                                          String companyId, String currency) {
        String key = String.format(FILTER_SHOES_DTO_KEY,
                name != null ? name : "null",
                brand != null ? brand : "null",
                minPrice != null ? minPrice : "null",
                maxPrice != null ? maxPrice : "null",
                categoryId != null ? categoryId : "null",
                companyId != null ? companyId : "null",
                currency);
        try {
            Object cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue != null && cachedValue instanceof List<?>) {
                log.debug("Cache hit for filter with currency: {}", currency);
                return (List<ShoeDto>) cachedValue;
            }
            log.debug("Cache miss for filter with currency: {}", currency);
            return null;
        } catch (Exception e) {
            log.error("Error retrieving filter results from cache: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Store filter results in cache
     *
     * @param name Filter by name
     * @param brand Filter by brand
     * @param minPrice Filter by min price
     * @param maxPrice Filter by max price
     * @param categoryId Filter by category ID
     * @param companyId Filter by company ID
     * @param currency Currency code
     * @param shoeDtos List of ShoeDtos to cache
     */
    public void cacheFilterResults(String name, String brand, String minPrice,
                                   String maxPrice, String categoryId,
                                   String companyId, String currency,
                                   List<ShoeDto> shoeDtos) {
        String key = String.format(FILTER_SHOES_DTO_KEY,
                name != null ? name : "null",
                brand != null ? brand : "null",
                minPrice != null ? minPrice : "null",
                maxPrice != null ? maxPrice : "null",
                categoryId != null ? categoryId : "null",
                companyId != null ? companyId : "null",
                currency);
        try {
            redisTemplate.opsForValue().set(key, new ArrayList<>(shoeDtos), CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.debug("Cached filter results with currency: {}", currency);
        } catch (Exception e) {
            log.error("Error caching filter results: {}", e.getMessage());
        }
    }

    /**
     * Clear all caches
     */
    public void clearAllCaches() {
        try {
            // We can only delete keys by pattern in Spring Data Redis using scan
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            log.info("All caches cleared");
        } catch (Exception e) {
            log.error("Error clearing all caches: {}", e.getMessage());
        }
    }

    /**
     * Clear cache for a specific shoe ID
     *
     * @param id Shoe ID
     */
    public void clearShoeCache(Long id) {
        try {
            // Delete all currency variants for this shoe
            redisTemplate.delete(String.format(SHOE_DTO_BY_ID_KEY, id, "*"));
            // Also clear all list caches
            clearAllListCaches();
            log.debug("Cache cleared for shoe ID: {}", id);
        } catch (Exception e) {
            log.error("Error clearing cache for shoe ID {}: {}", id, e.getMessage());
        }
    }

    /**
     * Clear all list caches (all shoes, search, filter)
     */
    public void clearAllListCaches() {
        try {
            // Clear all shoes lists, search results, and filter results
            redisTemplate.delete(ALL_SHOES_DTO_KEY.replace("%s", "*"));
            redisTemplate.delete(SEARCH_SHOES_DTO_KEY.replace("%s:%s", "*"));
            redisTemplate.delete(FILTER_SHOES_DTO_KEY.replace("%s:%s:%s:%s:%s:%s:%s", "*"));
            log.debug("All list caches cleared");
        } catch (Exception e) {
            log.error("Error clearing list caches: {}", e.getMessage());
        }
    }
}