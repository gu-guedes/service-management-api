package com.example.service_management.features.medicalrecord.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private String complaint;

    @NotBlank
    private String treatment;

    @DecimalMin("0.0")
    private BigDecimal weightKg;

    // opcional — nem todo atendimento precisa de lembrete de retorno
    private LocalDate followUpDate;

    // Boolean (nao boolean) porque o create nao manda esse campo — precisa aceitar ausente/null
    private Boolean followUpDone;
}
