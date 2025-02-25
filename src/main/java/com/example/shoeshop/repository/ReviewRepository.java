package com.example.shoeshop.repository;

import com.example.shoeshop.model.Review;
import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndShoe(User user, Shoe shoe);
}
