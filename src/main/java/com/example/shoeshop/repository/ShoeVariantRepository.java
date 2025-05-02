package com.example.shoeshop.repository;

import com.example.shoeshop.model.ShoeVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoeVariantRepository extends JpaRepository<ShoeVariant, Long> {
    List<ShoeVariant> findByShoeId(Long shoeId);
}
