package com.example.shoeshop.repository;

import com.example.shoeshop.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Custom query methods can be added here if needed
}
