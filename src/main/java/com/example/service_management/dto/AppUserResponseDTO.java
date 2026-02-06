package com.example.service_management.dto;

import java.time.OffsetDateTime;

public class AppUserResponseDTO {
    private final Long id;
    private final String username;
    private final Boolean active;
    private final OffsetDateTime createdAt;

    public AppUserResponseDTO(Long id, String username, Boolean active, OffsetDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Boolean getActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
