package com.example.service_management.features.customer.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    public Customer(String name, String email, String phone, String cpf, String street, String streetNumber,
                     String neighborhood, String city, String referencePoint) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
        this.street = street;
        this.streetNumber = streetNumber;
        this.neighborhood = neighborhood;
        this.city = city;
        this.referencePoint = referencePoint;
    }

    protected Customer() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;
    private String phone;

    @Column(name = "cpf", length = 11, unique = true)
    private String cpf;

    @Column(name = "street")
    private String street;

    @Column(name = "street_number")
    private String streetNumber;

    private String neighborhood;
    private String city;

    @Column(name = "reference_point")
    private String referencePoint;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // separado do soft-delete de Patient (active) — excluir precisa sumir de vez da lista,
    // sem apagar a linha (preserva historico)
    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    private void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getStreetNumber() { return streetNumber; }
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getReferencePoint() { return referencePoint; }
    public void setReferencePoint(String referencePoint) { this.referencePoint = referencePoint; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
