package com.example.shoeshop.model;

import com.example.shoeshop.deserializer.BigDecimalDeserializer;
import com.example.shoeshop.deserializer.StringTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shoe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String name;

    @Column(nullable = false)
    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String brand;

    @Column(nullable = false)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal price;

    @JsonDeserialize(using = StringTrimDeserializer.class)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Company company;

    @OneToMany(mappedBy = "shoe", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<ShoeVariant> variants = new ArrayList<>();
}
