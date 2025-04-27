package com.example.Minora.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.*;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id
    private String id;

    private LocalDateTime acceptDate;

    private String sourceCompany;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;
}
