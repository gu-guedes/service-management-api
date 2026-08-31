package com.example.service_management.features.productapplication.mapper;

import com.example.service_management.features.productapplication.dto.ProductApplicationRequestDTO;
import com.example.service_management.features.productapplication.dto.ProductApplicationResponseDTO;
import com.example.service_management.features.productapplication.model.ProductApplication;
import com.example.service_management.features.patient.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class ProductApplicationMapper {

    public ProductApplicationResponseDTO toResponse(ProductApplication p) {
        if (p == null) return null;
        return ProductApplicationResponseDTO.builder()
                .id(p.getId())
                .patientId(p.getPatient() != null ? p.getPatient().getId() : null)
                .patientName(p.getPatient() != null ? p.getPatient().getName() : null)
                .productName(p.getProductName())
                .appliedDate(p.getAppliedDate())
                .expiresAt(p.getExpiresAt())
                .notes(p.getNotes())
                .createdAt(p.getCreatedAt())
                .build();
    }

    public ProductApplication toEntity(ProductApplicationRequestDTO dto, Patient patient) {
        return new ProductApplication(patient, dto.getProductName(), dto.getAppliedDate(), dto.getExpiresAt(), dto.getNotes());
    }
}
