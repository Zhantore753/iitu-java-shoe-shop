package com.example.shoeshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal percentage;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "shoe_id")
    private Shoe shoe; // Nullable, means discount can be for specific shoe

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Nullable, means discount can be for a category

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company; // Nullable, means discount can be for a company
}
