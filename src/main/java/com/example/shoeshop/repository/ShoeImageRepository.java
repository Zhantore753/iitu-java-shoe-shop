package com.example.shoeshop.repository;

import com.example.shoeshop.model.ShoeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoeImageRepository extends JpaRepository<ShoeImage, Long> {
    List<ShoeImage> findByShoeVariantId(Long variantId);
}
