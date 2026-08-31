package com.example.service_management.features.medicalrecordimage.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordImageResponseDTO {
    private Long id;
    private Long medicalRecordId;
    private Long patientId;
    private String patientName;
    private String fileName;
    private String contentType;
    private OffsetDateTime createdAt;
}