package com.store.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.store.api.entity.Admin;
import com.store.api.repository.AdminRepository;

@Configuration
public class AdminSeedConfig {

    private static final Logger log = LoggerFactory.getLogger(AdminSeedConfig.class);

    @Bean
    public CommandLineRunner adminSeedRunner(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.seed.enabled:true}") boolean seedEnabled,
            @Value("${app.admin.seed.username:admin}") String seedUsername,
            @Value("${app.admin.seed.password:admin123}") String seedPassword) {

        return args -> {
            if (!seedEnabled) {
                return;
            }
            if (adminRepository.count() > 0) {
                return;
            }
            if (seedUsername == null || seedUsername.isBlank()
                    || seedPassword == null || seedPassword.isBlank()) {
                log.warn("Admin seed skipped: username or password empty. Configure app.admin.seed.*");
                return;
            }

            Admin admin = new Admin();
            admin.setNombre(seedUsername);
            admin.setPassword(passwordEncoder.encode(seedPassword));
            adminRepository.save(admin);

            log.info("Admin seed created with username: {}", seedUsername);
        };
    }
}
