package com.example.service_management.service;

import com.example.service_management.dto.AppUserResponseDTO;
import com.example.service_management.repository.AppUserRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public List<AppUserResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(user -> new AppUserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getActive(),
                user.getCreatedAt()
        )).toList();
    }

}