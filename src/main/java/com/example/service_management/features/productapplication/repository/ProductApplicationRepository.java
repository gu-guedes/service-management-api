package com.example.service_management.features.productapplication.repository;

import com.example.service_management.features.productapplication.model.ProductApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductApplicationRepository extends JpaRepository<ProductApplication, Long> {
    List<ProductApplication> findByPatientIdOrderByExpiresAtDesc(Long patientId);
}
