package com.example.service_management.service;

import com.example.service_management.dto.AppUserRequestDTO;
import com.example.service_management.dto.AppUserResponseDTO;
import com.example.service_management.model.AppUser;
import com.example.service_management.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    public AppUserResponseDTO create(AppUserRequestDTO dto) {
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    AppUser newAppUser = new AppUser(dto.getUsername(), hashedPassword);

    AppUser savedUser = repository.save(newAppUser);

    return new AppUserResponseDTO(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getActive(),
            savedUser.getCreatedAt()
    );

    }

}