package com.example.shoeshop.service;

import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.repository.ShoeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoeService {

    private final ShoeRepository shoeRepository;

    public Shoe createShoe(Shoe shoe) {
        return shoeRepository.save(shoe);
    }

    public List<Shoe> getAllShoes() {
        return shoeRepository.findAll();
    }

    public Shoe updateShoe(Long id, Shoe updatedShoe) {
        Shoe shoe = shoeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shoe not found"));
        shoe.setName(updatedShoe.getName());
        shoe.setBrand(updatedShoe.getBrand());
        shoe.setPrice(updatedShoe.getPrice());
        shoe.setCategory(updatedShoe.getCategory());
        shoe.setCompany(updatedShoe.getCompany());
        return shoeRepository.save(shoe);
    }

    public void deleteShoe(Long id) {
        shoeRepository.deleteById(id);
    }
}
