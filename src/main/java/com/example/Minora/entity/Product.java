package com.example.Minora.entity;

import com.example.Minora.config.LocalDateConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // бо product в XML не має ID

    private String type;

    private BigDecimal price;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
