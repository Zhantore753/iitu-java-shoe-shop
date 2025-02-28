package com.example.shoeshop.service;

import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.model.ShoeVariant;
import com.example.shoeshop.repository.ShoeRepository;
import com.example.shoeshop.repository.ShoeVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoeVariantService {

    private final ShoeVariantRepository shoeVariantRepository;
    private final ShoeRepository shoeRepository;

    public ShoeVariant addVariant(Long shoeId, ShoeVariant variant) {
        Shoe shoe = shoeRepository.findById(shoeId)
                .orElseThrow(() -> new IllegalArgumentException("Shoe not found"));
        variant.setShoe(shoe);
        return shoeVariantRepository.save(variant);
    }
}
