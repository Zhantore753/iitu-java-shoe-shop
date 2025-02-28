package com.example.shoeshop.service;

import com.example.shoeshop.model.ShoeImage;
import com.example.shoeshop.repository.ShoeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoeImageService {

    private final ShoeImageRepository shoeImageRepository;
    private final String UPLOAD_DIR = "uploads/";

    public ShoeImage uploadImage(Long shoeVariantId, MultipartFile file) {
        try {
            // Generate a unique filename
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(UPLOAD_DIR, fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            ShoeImage shoeImage = new ShoeImage();
            shoeImage.setImageUrl("/uploads/" + fileName);
            return shoeImageRepository.save(shoeImage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
