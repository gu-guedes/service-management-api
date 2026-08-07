package com.example.service_management.features.productapplication.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductApplicationResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private String productName;
    private LocalDate appliedDate;
    private LocalDate expiresAt;
    private String notes;
    private OffsetDateTime createdAt;
}
