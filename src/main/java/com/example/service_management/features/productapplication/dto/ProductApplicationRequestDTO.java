package com.example.service_management.features.productapplication.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductApplicationRequestDTO {

    @NotNull
    private Long patientId;

    @NotBlank
    private String productName;

    private LocalDate appliedDate;

    @NotNull
    private LocalDate expiresAt;

    private String notes;
}
