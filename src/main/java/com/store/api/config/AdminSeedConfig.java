package com.store.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
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
            JdbcTemplate jdbcTemplate,
            @Value("${app.admin.seed.enabled:true}") boolean seedEnabled,
            @Value("${app.admin.seed.username:admin}") String seedUsername,
            @Value("${app.admin.seed.password:admin123}") String seedPassword) {

        return args -> {
            if (!seedEnabled) {
                return;
            }

            // Run the seeding in a separate thread so it doesn't block application startup
            Runnable seedTask = () -> {
                try {
                    // small delay to let DB migrations/DDL finish if any
                    Thread.sleep(2000);

                    // Avoid blocking DB calls: first check if table `admin` exists
                    Boolean tableExists = false;
                    try {
                        String sql = "SELECT to_regclass('public.admin') IS NOT NULL";
                        tableExists = jdbcTemplate.queryForObject(sql, Boolean.class);
                    } catch (DataAccessException ex) {
                        log.warn("Admin seed skipped: DB not ready ({}).", ex.getMessage());
                        return;
                    }
                    if (Boolean.FALSE.equals(tableExists)) {
                        log.info("Admin table not present yet — skipping seed.");
                        return;
                    }

                    // Try to insert the admin with a single upsert-style statement to avoid
                    // extra round-trips and reduce chance of long-running locks.
                    if (seedUsername == null || seedUsername.isBlank()
                            || seedPassword == null || seedPassword.isBlank()) {
                        log.warn("Admin seed skipped: username or password empty. Configure app.admin.seed.*");
                        return;
                    }

                        String insertSql = "INSERT INTO admin (nombre, password_hash) " +
                            "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM admin)";
                    int rows = 0;
                    try {
                        rows = jdbcTemplate.update(insertSql, seedUsername, passwordEncoder.encode(seedPassword));
                    } catch (DataAccessException ex) {
                        log.warn("Admin seed skipped: insert failed ({}).", ex.getMessage());
                        return;
                    }

                    if (rows > 0) {
                        log.info("Admin seed created with username: {}", seedUsername);
                    } else {
                        log.info("Admin seed not required; an admin already exists.");
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("Admin seed thread interrupted");
                } catch (Exception e) {
                    log.error("Unexpected error running admin seed: {}", e.getMessage());
                }
            };

            Thread t = new Thread(seedTask, "admin-seed-thread");
            t.setDaemon(true);
            t.start();
        };
    }
}
