package com.example.service_management.features.patient.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {

    @NotNull
    private Long customerId;

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String species;

    @NotBlank
    @Size(max = 100)
    private String breed;

    @NotBlank
    private String sex;

    @NotNull
    @Min(0)
    @Max(50)
    private Integer ageYears;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal weightKg;

    private Boolean neutered;

    @NotBlank
    private String notes;

    private Boolean active;
}
