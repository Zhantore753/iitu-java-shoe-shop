package com.example.shoeshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shoe_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoe_variant_id", nullable = false)
    private ShoeVariant shoeVariant;

    @Column(nullable = false)
    private String imageUrl;
}
