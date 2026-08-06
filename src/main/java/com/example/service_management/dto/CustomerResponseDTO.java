package com.example.service_management.dto;

import java.time.OffsetDateTime;

public class CustomerResponseDTO {
    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final String street;
    private final String streetNumber;
    private final String neighborhood;
    private final String city;
    private final String referencePoint;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public CustomerResponseDTO(Long id, String name, String email, String phone, String street, String streetNumber,
                                String neighborhood, String city, String referencePoint,
                                OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.streetNumber = streetNumber;
        this.neighborhood = neighborhood;
        this.city = city;
        this.referencePoint = referencePoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStreet() { return street; }
    public String getStreetNumber() { return streetNumber; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getReferencePoint() { return referencePoint; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

}
