package com.example.service_management.features.examrequest.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRequestResponseDTO {
    private Long id;
    private Long medicalRecordId;
    private Long patientId;
    private String patientName;
    private String examName;
    private LocalDate requestedDate;
    private String resultFileName;
    private OffsetDateTime resultUploadedAt;
    private OffsetDateTime createdAt;
}
