package com.example.shoeshop.controller;

import com.example.shoeshop.model.*;
import com.example.shoeshop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ShoeService shoeService;
    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final ShoeVariantService shoeVariantService;
    private final ShoeImageService shoeImageService;

    @PostMapping("/shoes")
    public ResponseEntity<Shoe> createShoe(@RequestBody Shoe shoe) {
        return ResponseEntity.ok(shoeService.createShoe(shoe));
    }

    @GetMapping("/shoes")
    public ResponseEntity<List<Shoe>> getAllShoes() {
        return ResponseEntity.ok(shoeService.getAllShoes());
    }

    @PutMapping("/shoes/{id}")
    public ResponseEntity<Shoe> updateShoe(@PathVariable Long id, @RequestBody Shoe updatedShoe) {
        return ResponseEntity.ok(shoeService.updateShoe(id, updatedShoe));
    }

    @DeleteMapping("/shoes/{id}")
    public ResponseEntity<String> deleteShoe(@PathVariable Long id) {
        shoeService.deleteShoe(id);
        return ResponseEntity.ok("Shoe deleted successfully");
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping("/shoes/{shoeId}/variants")
    public ResponseEntity<ShoeVariant> addVariant(@PathVariable Long shoeId, @RequestBody ShoeVariant variant) {
        return ResponseEntity.ok(shoeVariantService.addVariant(shoeId, variant));
    }

    @PostMapping("/shoe/{variantId}/image")
    public ResponseEntity<ShoeImage> uploadImage(@PathVariable Long variantId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(shoeImageService.uploadImage(variantId, file));
    }
}
