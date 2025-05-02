package com.example.shoeshop.repository;

import com.example.shoeshop.model.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    
    // Search by query across multiple fields
    @Query("SELECT s FROM Shoe s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.brand) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Shoe> searchByQuery(@Param("query") String query);
    
    // Search by specific fields
    List<Shoe> findByNameContainingIgnoreCase(String name);
    List<Shoe> findByBrandContainingIgnoreCase(String brand);
    List<Shoe> findByDescriptionContainingIgnoreCase(String description);
    
    // Filter by price range
    List<Shoe> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Filter by category
    List<Shoe> findByCategoryId(Long categoryId);
    
    // Filter by company
    List<Shoe> findByCompanyId(Long companyId);
}
