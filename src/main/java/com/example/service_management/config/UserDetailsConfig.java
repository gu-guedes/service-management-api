package com.example.service_management.config;

import com.example.service_management.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService(AppUserRepository repo) {
        return username -> repo.findByUsername(username)
                .map(u -> User.builder()
                        .username(u.getUsername())
                        .password(u.getPasswordHash())
                        .disabled(Boolean.FALSE.equals(u.getActive()))
                        .authorities("ROLE_USER") // ajuste se tiver roles
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
