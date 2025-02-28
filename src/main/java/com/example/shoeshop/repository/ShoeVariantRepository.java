package com.example.shoeshop.repository;

import com.example.shoeshop.model.ShoeVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeVariantRepository extends JpaRepository<ShoeVariant, Long> {
}
