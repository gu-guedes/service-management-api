package com.example.service_management.service;

import com.example.service_management.dto.AppUserRequestDTO;
import com.example.service_management.dto.AppUserResponseDTO;
import com.example.service_management.exception.ResourceNotFoundException;
import com.example.service_management.mapper.AppUserMapper;
import com.example.service_management.model.AppUser;
import com.example.service_management.repository.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserMapper mapper;

    public AppUserService(AppUserRepository repository, PasswordEncoder passwordEncoder, AppUserMapper mapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public List<AppUserResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public AppUserResponseDTO create(AppUserRequestDTO dto) {
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        AppUser newAppUser = mapper.toEntity(dto);
        AppUser savedUser = repository.save(newAppUser);
        return mapper.toResponseDTO(savedUser);
    }

    public AppUserResponseDTO findById(Long id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with id " + id));
        return mapper.toResponseDTO(user);
    }

    public void delete(Long id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with id " + id));
        user.setActive(false);
        repository.save(user);
    }

    public AppUserResponseDTO update(Long id, @Valid AppUserRequestDTO appUser) {
        AppUser existingUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with id " + id));

        existingUser.setUsername(appUser.getUsername());
        if (appUser.getPassword() != null && !appUser.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(appUser.getPassword());
            existingUser.setPasswordHash(hashedPassword);
        }

        AppUser updatedUser = repository.save(existingUser);

        return mapper.toResponseDTO(updatedUser);
    }
}