package com.example.shoeshop.dto;

import com.example.shoeshop.deserializer.BigDecimalDeserializer;
import com.example.shoeshop.serializer.BigDecimalSerializer;
import com.example.shoeshop.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoeDto {
    @JsonView(Views.Public.class)
    private Long id;
    
    @JsonView(Views.Public.class)
    private String name;
    
    @JsonView(Views.Public.class)
    private String brand;
    
    @JsonView(Views.Public.class)
    @JsonSerialize(using = BigDecimalSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal price;
    
    @JsonView(Views.Public.class)
    private String currency = "USD";
    
    @JsonView(Views.Extended.class)
    private String description;
    
    @JsonView(Views.Admin.class)
    private Long categoryId;
    
    @JsonView(Views.Admin.class)
    private Long companyId;
}
