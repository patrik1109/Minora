package com.example.Minora.service;

import com.example.Minora.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, String> {
}
