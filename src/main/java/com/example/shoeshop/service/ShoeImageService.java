package com.example.shoeshop.service;

import com.example.shoeshop.model.ShoeImage;
import com.example.shoeshop.model.ShoeVariant;
import com.example.shoeshop.repository.ShoeImageRepository;
import com.example.shoeshop.repository.ShoeVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoeImageService {

    private final ShoeImageRepository shoeImageRepository;
    private final ShoeVariantRepository shoeVariantRepository;

    public ShoeImage uploadImage(Long variantId, MultipartFile file) {
        try {
            ShoeVariant variant = shoeVariantRepository.findById(variantId)
                    .orElseThrow(() -> new IllegalArgumentException("Shoe variant not found"));
            
            ShoeImage image = new ShoeImage();
            image.setShoeVariant(variant);
            image.setImageData(file.getBytes());
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            
            return shoeImageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public List<ShoeImage> getImagesByVariantId(Long variantId) {
        return shoeImageRepository.findByShoeVariantId(variantId);
    }
    
    public void deleteImage(Long id) {
        shoeImageRepository.deleteById(id);
    }
}
