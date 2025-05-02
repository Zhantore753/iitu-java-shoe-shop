package com.example.shoeshop.mapper;

import com.example.shoeshop.dto.ShoeDto;
import com.example.shoeshop.model.Shoe;
import com.example.shoeshop.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ShoeMapper {

    private final CurrencyService currencyService;

    public ShoeDto toDto(Shoe shoe) {
        return toDto(shoe, "USD");
    }

    public ShoeDto toDto(Shoe shoe, String currency) {
        if (shoe == null) {
            return null;
        }

        ShoeDto dto = new ShoeDto();
        dto.setId(shoe.getId());
        dto.setName(shoe.getName());
        dto.setBrand(shoe.getBrand());
        
        // Convert price to requested currency
        BigDecimal convertedPrice = currencyService.convertPrice(shoe.getPrice(), currency);
        dto.setPrice(convertedPrice);
        dto.setCurrency(currency);
        
        dto.setDescription(shoe.getDescription());

        if (shoe.getCategory() != null) {
            dto.setCategoryId(shoe.getCategory().getId());
        }

        if (shoe.getCompany() != null) {
            dto.setCompanyId(shoe.getCompany().getId());
        }

        return dto;
    }
}
