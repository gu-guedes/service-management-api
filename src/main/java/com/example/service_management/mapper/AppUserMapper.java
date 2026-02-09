package com.example.service_management.mapper;

import com.example.service_management.dto.AppUserRequestDTO;
import com.example.service_management.dto.AppUserResponseDTO;
import com.example.service_management.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUserResponseDTO toResponseDTO(AppUser appUser) {
        return new AppUserResponseDTO(
            appUser.getId(),
            appUser.getUsername(),
            appUser.getActive(),
            appUser.getCreatedAt(),
            appUser.getUpdatedAt()
        );
    }

     public AppUser toEntity(AppUserRequestDTO dto) {
        return new AppUser(dto.getUsername(), dto.getPassword());
    }
}
