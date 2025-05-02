package com.example.shoeshop.controller;

import com.example.shoeshop.dto.ShoeDto;
import com.example.shoeshop.exception.InvalidInputException;
import com.example.shoeshop.mapper.ShoeMapper;
import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.service.CurrencyService;
import com.example.shoeshop.service.ShoeService;
import com.example.shoeshop.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final ShoeService shoeService;
    private final ShoeMapper shoeMapper;
    private final CurrencyService currencyService;

    @JsonView(Views.Public.class)
    @GetMapping("/shoes")
    public ResponseEntity<List<ShoeDto>> getAllShoes(
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        // Use cached DTO list instead of retrieving and mapping entities
        return ResponseEntity.ok(shoeService.getAllShoeDtos(currency));
    }

    @JsonView(Views.Extended.class)
    @GetMapping("/shoes/{id}")
    public ResponseEntity<ShoeDto> getShoeById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        // Use cached DTO instead of retrieving and mapping entity
        return ResponseEntity.ok(shoeService.getShoeDtoById(id, currency));
    }
    
    /**
     * Search for shoes using a general query parameter
     * This endpoint searches across name, brand, and description
     * 
     * @param query The search query
     * @param currency The currency to display prices in
     * @return List of matching ShoeDto objects
     */
    @JsonView(Views.Extended.class)
    @GetMapping("/shoes/search")
    public ResponseEntity<?> searchShoes(
            @RequestParam(required = true) String query,
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        try {
            // Use cached search results instead of retrieving and mapping entities
            List<ShoeDto> shoeDtos = shoeService.searchShoeDtos(query, currency);
            log.info("Found {} shoes matching query: {}, currency: {}", shoeDtos.size(), query, currency);
            return ResponseEntity.ok(shoeDtos);
        } catch (InvalidInputException e) {
            log.warn("Invalid search query: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Filter shoes by various criteria
     * 
     * @param name The name to search for
     * @param brand The brand to search for  
     * @param minPrice Minimum price (in USD)
     * @param maxPrice Maximum price (in USD)
     * @param categoryId Category ID to filter by
     * @param companyId Company ID to filter by
     * @param currency The currency to display prices in
     * @return List of matching ShoeDto objects
     */
    @JsonView(Views.Extended.class)
    @GetMapping("/shoes/filter")
    public ResponseEntity<List<ShoeDto>> filterShoes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false, defaultValue = "USD") String currency) {
        
        // Use cached filter results instead of retrieving and mapping entities
        List<ShoeDto> shoeDtos = shoeService.filterShoeDtos(name, brand, minPrice, 
                                                          maxPrice, categoryId, companyId, currency);
                
        log.info("Found {} shoes matching filters, currency: {}", shoeDtos.size(), currency);
        return ResponseEntity.ok(shoeDtos);
    }
    
    /**
     * Get available currencies and their exchange rates
     * 
     * @return Map of currency codes to exchange rates
     */
    @GetMapping("/currencies")
    public ResponseEntity<Map<String, Object>> getAvailableCurrencies() {
        return ResponseEntity.ok(currencyService.getExchangeRates());
    }
}
