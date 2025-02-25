package com.example.shoeshop.repository;

import com.example.shoeshop.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
