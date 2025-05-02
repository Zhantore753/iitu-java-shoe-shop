package com.example.shoeshop.config;

import com.example.shoeshop.model.Category;
import com.example.shoeshop.model.Company;
import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.repository.CategoryRepository;
import com.example.shoeshop.repository.CompanyRepository;
import com.example.shoeshop.repository.ShoeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after AdminSeed which is Order(1)
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final ShoeRepository shoeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if we already have data
        if (categoryRepository.count() > 0 && companyRepository.count() > 0 && shoeRepository.count() > 0) {
            log.info("Database already contains data, skipping initialization");
            return;
        }

        log.info("Initializing sample data for testing search functionality...");

        try {
            // First create and save categories
            Category running = new Category();
            running.setName("Running");
            running.setDescription("Shoes designed for running");
            running.setIcon("run_icon.png");
            running.setActive(true);
            categoryRepository.save(running);
            
            Category casual = new Category();
            casual.setName("Casual");
            casual.setDescription("Everyday casual shoes");
            casual.setIcon("casual_icon.png");
            casual.setActive(true);
            categoryRepository.save(casual);
            
            Category formal = new Category();
            formal.setName("Formal");
            formal.setDescription("Business and formal occasions");
            formal.setIcon("formal_icon.png");
            formal.setActive(true);
            categoryRepository.save(formal);
            
            Category sport = new Category();
            sport.setName("Sport");
            sport.setDescription("Athletic performance shoes");
            sport.setIcon("sport_icon.png");
            sport.setActive(true);
            categoryRepository.save(sport);
            
            Category hiking = new Category();
            hiking.setName("Hiking");
            hiking.setDescription("Outdoor and trail shoes");
            hiking.setIcon("hiking_icon.png");
            hiking.setActive(true);
            categoryRepository.save(hiking);
            
            log.info("Categories created successfully");
            
            // Then create and save companies
            Company nike = new Company();
            nike.setName("Nike");
            nike.setDescription("American athletic footwear company");
            nike.setLogo("nike_logo.png");
            nike.setWebsite("https://nike.com");
            companyRepository.save(nike);
            
            Company adidas = new Company();
            adidas.setName("Adidas");
            adidas.setDescription("German sportswear manufacturer");
            adidas.setLogo("adidas_logo.png");
            adidas.setWebsite("https://adidas.com");
            companyRepository.save(adidas);
            
            Company newBalance = new Company();
            newBalance.setName("New Balance");
            newBalance.setDescription("American sports footwear manufacturer");
            newBalance.setLogo("nb_logo.png");
            newBalance.setWebsite("https://newbalance.com");
            companyRepository.save(newBalance);
            
            Company puma = new Company();
            puma.setName("Puma");
            puma.setDescription("German multinational athletic wear");
            puma.setLogo("puma_logo.png");
            puma.setWebsite("https://puma.com");
            companyRepository.save(puma);
            
            Company timberland = new Company();
            timberland.setName("Timberland");
            timberland.setDescription("American outdoor wear company");
            timberland.setLogo("timberland_logo.png");
            timberland.setWebsite("https://timberland.com");
            companyRepository.save(timberland);
            
            log.info("Companies created successfully");
            
            // Create and save shoes one by one
            createAndSaveShoe("Air Max 270", "Nike", new BigDecimal("129.99"), 
                    "Featuring Nike's biggest heel Air unit yet, the Air Max 270 delivers visible cushioning under every step.", 
                    running, nike);
            
            createAndSaveShoe("Ultra Boost", "Adidas", new BigDecimal("179.99"), 
                    "Experience incredible energy return with the Ultra Boost running shoes.", 
                    running, adidas);
                    
            createAndSaveShoe("Fresh Foam 1080", "New Balance", new BigDecimal("149.99"), 
                    "The Fresh Foam 1080 gives runners premium cushioning with a soft, smooth ride.", 
                    running, newBalance);
                    
            createAndSaveShoe("RS-X", "Puma", new BigDecimal("110.00"), 
                    "The RS-X reimagines Puma's 80s Running System with bold design and cushioning for extreme comfort.", 
                    casual, puma);
            
            createAndSaveShoe("Stan Smith", "Adidas", new BigDecimal("85.00"), 
                    "A tennis classic turned fashion staple.", 
                    casual, adidas);
                    
            createAndSaveShoe("Classic Leather", "Puma", new BigDecimal("75.00"), 
                    "Timeless design with premium materials.", 
                    casual, puma);
                    
            log.info("Sample data initialization complete. Created {} shoes, {} categories, and {} companies",
                    shoeRepository.count(), categoryRepository.count(), companyRepository.count());
                    
        } catch (Exception e) {
            log.error("Error during data initialization", e);
            throw e;
        }
    }
    
    private void createAndSaveShoe(String name, String brand, BigDecimal price, String description,
                                  Category category, Company company) {
        Shoe shoe = new Shoe();
        shoe.setName(name);
        shoe.setBrand(brand);
        shoe.setPrice(price);
        shoe.setDescription(description);
        shoe.setCategory(category);
        shoe.setCompany(company);
        shoeRepository.save(shoe);
        log.debug("Created shoe: {}", name);
    }
}
