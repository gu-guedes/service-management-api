package com.example.service_management.controller;

import com.example.service_management.dto.AppUserResponseDTO;
import com.example.service_management.service.AppUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
