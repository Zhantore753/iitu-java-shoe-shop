package com.example.shoeshop.repository;

import com.example.shoeshop.model.ShoeVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeColorRepository extends JpaRepository<ShoeVariant, Long> {
}
