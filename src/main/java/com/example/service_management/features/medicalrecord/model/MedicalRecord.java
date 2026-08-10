package com.example.service_management.features.medicalrecord.model;

import com.example.service_management.features.patient.model.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "record_date", nullable = false)
    private OffsetDateTime recordDate;

    @Column(nullable = false, columnDefinition = "text")
    private String complaint;

    @Column(nullable = false, columnDefinition = "text")
    private String anamnesis;

    @Column(nullable = false, columnDefinition = "text")
    private String treatment;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    // opcional — data escolhida pelo vet pra lembrar de avisar o tutor (ex: fim de um tratamento)
    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "follow_up_done", nullable = false)
    private boolean followUpDone;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        if (this.recordDate == null) {
            this.recordDate = now;
        }
    }
}
