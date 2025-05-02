package com.example.shoeshop.service;

import com.example.shoeshop.dto.ShoeDto;
import com.example.shoeshop.exception.InvalidInputException;
import com.example.shoeshop.exception.ResourceNotFoundException;
import com.example.shoeshop.mapper.ShoeMapper;
import com.example.shoeshop.model.Category;
import com.example.shoeshop.model.Company;
import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.repository.CategoryRepository;
import com.example.shoeshop.repository.CompanyRepository;
import com.example.shoeshop.repository.ShoeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoeService {

    private final ShoeRepository shoeRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final ShoeMapper shoeMapper;
    private final CurrencyService currencyService;

    public Shoe createShoe(Shoe shoe) {
        validateShoe(shoe);
        log.info("Creating new shoe: {}", shoe.getName());
        return shoeRepository.save(shoe);
    }

    /**
     * Get all shoes without using cache
     */
    public List<Shoe> getAllShoes() {
        log.info("Retrieving all shoes from database");
        return shoeRepository.findAll();
    }

    /**
     * Get all shoes and return as DTOs (cached version)
     */
    @Cacheable(value = "allShoesDtos")
    public List<ShoeDto> getAllShoeDtos(String currency) {
        log.info("Retrieving all shoes from database and converting to DTOs with currency {}", currency);
        List<Shoe> shoes = shoeRepository.findAll();
        return shoes.stream()
                .map(shoe -> shoeMapper.toDto(shoe, currency))
                .collect(Collectors.toList());
    }

    /**
     * Get a shoe by ID (entity version, not cached)
     */
    public Shoe getShoeById(Long id) {
        log.info("Retrieving shoe with id: {} from database", id);
        return shoeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe", id));
    }

    /**
     * Get a shoe by ID and return as DTO (cached version)
     */
    @Cacheable(value = "shoeDtoById", key = "{ #id, #currency }")
    public ShoeDto getShoeDtoById(Long id, String currency) {
        log.info("Retrieving shoe with id: {} from database and converting to DTO with currency {}", id, currency);
        Shoe shoe = shoeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shoe", id));
        return shoeMapper.toDto(shoe, currency);
    }

    @CacheEvict(value = {"shoeDtoById", "allShoesDtos", "shoeSearchDtos", "shoeFilterDtos"}, allEntries = true)
    @Transactional
    public Shoe updateShoe(Long id, Shoe updatedShoe) {
        log.info("Updating shoe with id: {} and clearing related caches", id);
        Shoe shoe = getShoeById(id);
        
        if (updatedShoe.getName() != null) {
            shoe.setName(updatedShoe.getName());
        }
        
        if (updatedShoe.getBrand() != null) {
            shoe.setBrand(updatedShoe.getBrand());
        }
        
        if (updatedShoe.getPrice() != null) {
            if (updatedShoe.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidInputException("Price cannot be negative");
            }
            shoe.setPrice(updatedShoe.getPrice());
        }
        
        if (updatedShoe.getDescription() != null) {
            shoe.setDescription(updatedShoe.getDescription());
        }
        
        if (updatedShoe.getCategory() != null && updatedShoe.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedShoe.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", updatedShoe.getCategory().getId()));
            shoe.setCategory(category);
        }
        
        if (updatedShoe.getCompany() != null && updatedShoe.getCompany().getId() != null) {
            Company company = companyRepository.findById(updatedShoe.getCompany().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", updatedShoe.getCompany().getId()));
            shoe.setCompany(company);
        }
        
        return shoeRepository.save(shoe);
    }
    
    @CacheEvict(value = {"shoeDtoById", "allShoesDtos", "shoeSearchDtos", "shoeFilterDtos"}, allEntries = true)
    @Transactional
    public Shoe updateShoeCategory(Long shoeId, Long categoryId) {
        log.info("Updating category to {} for shoe with id: {} and clearing related caches", categoryId, shoeId);
        Shoe shoe = getShoeById(shoeId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
        
        shoe.setCategory(category);
        return shoeRepository.save(shoe);
    }
    
    @CacheEvict(value = {"shoeDtoById", "allShoesDtos", "shoeSearchDtos", "shoeFilterDtos"}, allEntries = true)
    @Transactional
    public Shoe updateShoeCompany(Long shoeId, Long companyId) {
        log.info("Updating company to {} for shoe with id: {} and clearing related caches", companyId, shoeId);
        Shoe shoe = getShoeById(shoeId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", companyId));
        
        shoe.setCompany(company);
        return shoeRepository.save(shoe);
    }

    @CacheEvict(value = {"shoeDtoById", "allShoesDtos", "shoeSearchDtos", "shoeFilterDtos"}, allEntries = true)
    public void deleteShoe(Long id) {
        log.info("Deleting shoe with id: {} and clearing related caches", id);
        if (!shoeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shoe", id);
        }
        shoeRepository.deleteById(id);
    }
    
    private void validateShoe(Shoe shoe) {
        if (shoe.getName() == null || shoe.getName().trim().isEmpty()) {
            throw new InvalidInputException("Shoe name cannot be empty");
        }
        
        if (shoe.getBrand() == null || shoe.getBrand().trim().isEmpty()) {
            throw new InvalidInputException("Shoe brand cannot be empty");
        }
        
        if (shoe.getPrice() == null || shoe.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Shoe price must be positive");
        }
        
        if (shoe.getCategory() == null || shoe.getCategory().getId() == null) {
            throw new InvalidInputException("Shoe category cannot be null");
        }
        
        if (shoe.getCompany() == null || shoe.getCompany().getId() == null) {
            throw new InvalidInputException("Shoe company cannot be null");
        }
    }
    
    /**
     * Search for shoes (entity version, not cached)
     */
    public List<Shoe> searchShoes(String query) {
        log.info("Searching shoes with query: {} from database", query);
        
        if (!StringUtils.hasText(query) || query.length() < 2) {
            log.warn("Search query is too short: {}", query);
            throw new InvalidInputException("Search query must be at least 2 characters long");
        }
        
        return shoeRepository.searchByQuery(query);
    }
    
    /**
     * Search for shoes and return as DTOs (cached version)
     */
    @Cacheable(value = "shoeSearchDtos", key = "{ #query, #currency }", unless = "#result.isEmpty()")
    public List<ShoeDto> searchShoeDtos(String query, String currency) {
        log.info("Searching shoes with query: {} from database and converting to DTOs with currency {}", query, currency);
        
        if (!StringUtils.hasText(query) || query.length() < 2) {
            log.warn("Search query is too short: {}", query);
            throw new InvalidInputException("Search query must be at least 2 characters long");
        }
        
        List<Shoe> shoes = shoeRepository.searchByQuery(query);
        return shoes.stream()
                .map(shoe -> shoeMapper.toDto(shoe, currency))
                .collect(Collectors.toList());
    }
    
    /**
     * Filter shoes (entity version, not cached)
     */
    public List<Shoe> filterShoes(String name, String brand, BigDecimal minPrice, 
                                BigDecimal maxPrice, Long categoryId, Long companyId) {
        log.info("Filtering shoes from database with criteria: name={}, brand={}, price range={}–{}, categoryId={}, companyId={}", 
                name, brand, minPrice, maxPrice, categoryId, companyId);
        
        // If name is provided, search by name
        if (StringUtils.hasText(name)) {
            return shoeRepository.findByNameContainingIgnoreCase(name);
        }
        
        // If brand is provided, search by brand
        if (StringUtils.hasText(brand)) {
            return shoeRepository.findByBrandContainingIgnoreCase(brand);
        }
        
        // If price range is provided, filter by price
        if (minPrice != null && maxPrice != null) {
            return shoeRepository.findByPriceBetween(minPrice, maxPrice);
        }
        
        // If category is provided, filter by category
        if (categoryId != null) {
            return shoeRepository.findByCategoryId(categoryId);
        }
        
        // If company is provided, filter by company
        if (companyId != null) {
            return shoeRepository.findByCompanyId(companyId);
        }
        
        // If no filters provided, return empty list
        return Collections.emptyList();
    }
    
    /**
     * Filter shoes and return as DTOs (cached version)
     */
    @Cacheable(value = "shoeFilterDtos", key = "{ #name, #brand, #minPrice, #maxPrice, #categoryId, #companyId, #currency }", unless = "#result.isEmpty()")
    public List<ShoeDto> filterShoeDtos(String name, String brand, BigDecimal minPrice, 
                                BigDecimal maxPrice, Long categoryId, Long companyId, String currency) {
        log.info("Filtering shoes from database with criteria: name={}, brand={}, price range={}–{}, categoryId={}, companyId={}, currency={}", 
                name, brand, minPrice, maxPrice, categoryId, companyId, currency);
        
        List<Shoe> shoes = filterShoes(name, brand, minPrice, maxPrice, categoryId, companyId);
        return shoes.stream()
                .map(shoe -> shoeMapper.toDto(shoe, currency))
                .collect(Collectors.toList());
    }
}
