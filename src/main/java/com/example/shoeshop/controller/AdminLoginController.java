package com.example.shoeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {
    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin/login";
    }
}
