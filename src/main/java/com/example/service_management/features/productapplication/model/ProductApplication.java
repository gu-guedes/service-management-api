package com.example.service_management.features.productapplication.model;

import com.example.service_management.features.patient.model.Patient;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "product_applications")
public class ProductApplication {

    public ProductApplication(Patient patient, String productName, LocalDate appliedDate, LocalDate expiresAt, String notes) {
        this.patient = patient;
        this.productName = productName;
        this.appliedDate = appliedDate;
        this.expiresAt = expiresAt;
        this.notes = notes;
    }

    protected ProductApplication() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "product_name", nullable = false)
    private String productName;

    // opcional — se nao vier, assume a data do registro (ver onCreate)
    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Column(name = "expires_at", nullable = false)
    private LocalDate expiresAt;

    @Column(columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = OffsetDateTime.now();
        if (this.appliedDate == null) {
            this.appliedDate = LocalDate.now();
        }
    }

    public Long getId() { return id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public LocalDate getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDate expiresAt) { this.expiresAt = expiresAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}
