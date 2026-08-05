package com.example.service_management.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDTO {

    @NotNull
    private Long patientId;

    private OffsetDateTime recordDate;

    @NotBlank
    private String description;

    @DecimalMin("0.0")
    private BigDecimal weightKg;
}
