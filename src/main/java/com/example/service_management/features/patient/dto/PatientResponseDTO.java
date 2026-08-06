package com.example.service_management.features.patient.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {
    private Long id;
    private Long customerId;
    private String name;
    private String species;
    private String breed;
    private String sex;
    private Integer ageYears;
    private BigDecimal weightKg;
    private boolean neutered;
    private String notes;
    private boolean active;
    private OffsetDateTime createdAt;
}
