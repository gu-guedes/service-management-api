package com.example.service_management.controller;

import com.example.service_management.dto.AppUserRequestDTO;
import com.example.service_management.dto.AppUserResponseDTO;
import com.example.service_management.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app-users")

public class AppUserController {
private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUserResponseDTO> getAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponseDTO create(@Valid @RequestBody AppUserRequestDTO appUser) {
        return service.create(appUser);

    }
    @GetMapping("/{id}")
    public AppUserResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

}
