package com.example.service_management.service;

import com.example.service_management.dto.LoginRequestDTO;
import com.example.service_management.dto.LoginResponseDTO;
import com.example.service_management.model.AppUser;
import com.example.service_management.repository.AppUserRepository;
import com.example.service_management.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
   private final AppUserRepository repository;
   private final PasswordEncoder passwordEncoder;
   private final JwtUtil jwtUtil;

    public AuthService(AppUserRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        AppUser user = repository.findByUsername(dto.username())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if(Boolean.FALSE.equals(user.getActive())){
            throw new DisabledException("Usuário inativo");
        }

        boolean passwordOk = passwordEncoder.matches(dto.password(), user.getPasswordHash());
        if(!passwordOk){
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new LoginResponseDTO("Bearer", token);
    }
}
