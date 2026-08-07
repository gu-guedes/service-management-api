package com.example.service_management.features.examrequest.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRequestRequestDTO {

    @NotNull
    private Long medicalRecordId;

    @NotBlank
    private String examName;

    private LocalDate requestedDate;
}
