package com.example.Minora.service;

import com.example.Minora.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByEventIdIn(List<String> eventIds);
}
