package com.example.shoeshop.repository;

import com.example.shoeshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Custom query methods can be added here if needed
}
