package com.example.service_management.features.medicalrecord.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private OffsetDateTime recordDate;
    private String complaint;
    private String treatment;
    private BigDecimal weightKg;
    private LocalDate followUpDate;
    private boolean followUpDone;
    private OffsetDateTime createdAt;
}
