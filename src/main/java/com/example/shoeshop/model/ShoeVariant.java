package com.example.shoeshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shoe_variants") // Renamed from "shoe_colors" to reflect its role
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoe_id", nullable = false)
    private Shoe shoe;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String colorName;

    @Column(nullable = false, length = 7)
    private String hexCode;

    @Column(nullable = false)
    private int stockQuantity;
}
