package com.example.service_management.dto;

import java.time.OffsetDateTime;

public class CustomerResponseDTO {
    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public CustomerResponseDTO(Long id, String name, String email, String phone, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

}
