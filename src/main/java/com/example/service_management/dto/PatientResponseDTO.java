package com.example.service_management.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate birthDate;
    private BigDecimal weightKg;
    private boolean neutered;
    private String notes;
    private boolean active;
}
