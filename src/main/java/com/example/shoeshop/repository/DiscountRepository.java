package com.example.shoeshop.repository;

import com.example.shoeshop.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByShoeId(Long shoeId);
    List<Discount> findByCategoryId(Long categoryId);
    List<Discount> findByCompanyId(Long companyId);
}
