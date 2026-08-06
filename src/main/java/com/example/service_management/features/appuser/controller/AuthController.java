package com.example.service_management.features.appuser.controller;

import com.example.service_management.features.appuser.dto.LoginRequestDTO;
import com.example.service_management.features.appuser.dto.LoginResponseDTO;
import com.example.service_management.features.appuser.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }


    @PostMapping({"/login"})
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return service.login(dto);
    }
}
