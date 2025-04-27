package com.example.Minora.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyProductsDTO {
    private String sourceCompany;
    private List<ProductDto> products;

    public CompanyProductsDTO(String key, List<ProductDto> value) {
        this.sourceCompany = key;
        this.products = value;
    }

    public CompanyProductsDTO() {
    }
}
