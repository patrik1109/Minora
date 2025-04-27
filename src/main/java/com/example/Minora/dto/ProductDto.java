package com.example.Minora.dto;


import com.example.Minora.config.LocalDateAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data

@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDto {
    private String type;
    private BigDecimal price;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;

    public ProductDto(String type, BigDecimal price, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ProductDto() {

    }

}
