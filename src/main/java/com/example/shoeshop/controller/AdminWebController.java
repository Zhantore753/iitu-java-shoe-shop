package com.example.shoeshop.controller;

import com.example.shoeshop.model.*;
import com.example.shoeshop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {
    private final ShoeService shoeService;
    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final ShoeVariantService shoeVariantService;
    private final ShoeImageService shoeImageService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("shoes", shoeService.getAllShoes());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "admin/dashboard"; // Make sure to have an "admin/dashboard.html" file in templates
    }

    @PostMapping("/shoes")
    public String createShoe(@ModelAttribute Shoe shoe) {
        shoeService.createShoe(shoe);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/categories")
    public String createCategory(@ModelAttribute Category category) {
        categoryService.createCategory(category);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/companies")
    public String createCompany(@ModelAttribute Company company) {
        companyService.createCompany(company);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/shoes/{shoeId}/variants")
    public String addVariant(@PathVariable Long shoeId, @ModelAttribute ShoeVariant variant) {
        shoeVariantService.addVariant(shoeId, variant);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/shoe/{variantId}/image")
    public String uploadImage(@PathVariable Long variantId, @RequestParam("file") MultipartFile file) {
        shoeImageService.uploadImage(variantId, file);
        return "redirect:/admin/dashboard";
    }
}
