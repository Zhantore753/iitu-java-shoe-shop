package com.example.shoeshop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "shoe_variant_id", nullable = false) // Updated reference to ShoeVariant
    private ShoeVariant shoeVariant;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;
}
