package com.example.service_management.dto;

import java.time.OffsetDateTime;

public class AppUserResponseDTO {
    private final Long id;
    private final String username;
    private final Boolean active;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public AppUserResponseDTO(Long id, String username, Boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Boolean getActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
