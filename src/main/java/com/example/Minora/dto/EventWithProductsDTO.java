package com.example.Minora.dto;

import com.example.Minora.config.LocalDateAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EventWithProductsDTO {
    private String eventId;
    private String insuredId;
    private String eventType;
    private String productId;
    private String productType;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;


}
