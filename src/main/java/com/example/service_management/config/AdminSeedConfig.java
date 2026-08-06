package com.example.service_management.config;

import com.example.service_management.features.appuser.model.AppUser;
import com.example.service_management.features.appuser.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// -------------------------------------------------------------------
// Cria o primeiro usuario (admin) se a tabela app_user estiver vazia.
// Resolve o problema de "ovo e galinha": sem isso nao ha como logar
// e obter um token pra cadastrar os demais usuarios via POST /app-users.
// -------------------------------------------------------------------
@Configuration
public class AdminSeedConfig {

    @Bean
    public CommandLineRunner seedAdminUser(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed-admin.username}") String username,
            @Value("${app.seed-admin.password}") String password
    ) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new AppUser(username, passwordEncoder.encode(password)));
            }
        };
    }
}
