package com.example.shoeshop.controller;

import com.example.shoeshop.dto.ShoeDto;
import com.example.shoeshop.mapper.ShoeMapper;
import com.example.shoeshop.service.CurrencyService;
import com.example.shoeshop.service.ShoeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cache", description = "Endpoints for viewing cached data")
public class CacheController {

    private final ShoeService shoeService;
    private final CurrencyService currencyService;
    private final CacheManager cacheManager;

    @Operation(summary = "Get a shoe from cache by ID")
    @GetMapping("/shoes/{id}")
    public ResponseEntity<ShoeDto> getCachedShoe(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        log.info("Retrieving cached shoe with id: {}", id);
        ShoeDto shoeDto = shoeService.getShoeDtoById(id, currency);
        return ResponseEntity.ok(shoeDto);
    }

    @Operation(summary = "Get all shoes from cache")
    @GetMapping("/shoes")
    public ResponseEntity<List<ShoeDto>> getAllCachedShoes(
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        log.info("Retrieving all cached shoes");
        List<ShoeDto> shoeDtos = shoeService.getAllShoeDtos(currency);
        return ResponseEntity.ok(shoeDtos);
    }

    @Operation(summary = "Get cached search results")
    @GetMapping("/search")
    public ResponseEntity<List<ShoeDto>> getCachedSearchResults(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        log.info("Retrieving cached search results for query: {}", query);
        List<ShoeDto> shoeDtos = shoeService.searchShoeDtos(query, currency);
        return ResponseEntity.ok(shoeDtos);
    }
    
    @Operation(summary = "Get cached filter results")
    @GetMapping("/filter")
    public ResponseEntity<List<ShoeDto>> getCachedFilterResults(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        log.info("Retrieving cached filter results");
        List<ShoeDto> shoeDtos = shoeService.filterShoeDtos(name, brand, minPrice, 
                                                          maxPrice, categoryId, companyId, currency);
        return ResponseEntity.ok(shoeDtos);
    }

    @Operation(summary = "Get cached exchange rates")
    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Object>> getCachedExchangeRates() {
        log.info("Retrieving cached exchange rates");
        return ResponseEntity.ok(currencyService.getExchangeRates());
    }

    @Operation(summary = "Get cache statistics")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // List all available cache names
        Collection<String> cacheNames = cacheManager.getCacheNames();
        stats.put("availableCaches", cacheNames);
        
        // Include timestamp
        stats.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(stats);
    }
    
    @Operation(summary = "Clear all caches")
    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearAllCaches() {
        log.info("Clearing all caches");
        Collection<String> cacheNames = cacheManager.getCacheNames();
        
        for (String cacheName : cacheNames) {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("clearedCaches", cacheNames);
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(result);
    }
}