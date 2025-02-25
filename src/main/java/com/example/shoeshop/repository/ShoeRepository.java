package com.example.shoeshop.repository;

import com.example.shoeshop.model.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeRepository extends JpaRepository<Shoe, Long> {
}
