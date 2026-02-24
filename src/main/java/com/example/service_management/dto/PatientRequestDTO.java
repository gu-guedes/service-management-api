package com.example.service_management.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Size(max = 100)
    private String breed;

    private String sex;

    private LocalDate birthDate;

    @DecimalMin("0.0")
    private BigDecimal weightKg;

    private boolean neutered;

    private String notes;

    private boolean active;
}
