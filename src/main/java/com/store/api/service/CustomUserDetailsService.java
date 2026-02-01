package com.store.api.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.store.api.entity.Admin;
import com.store.api.repository.AdminRepository;

/**
 * Servicio para cargar detalles de usuario para autenticación.
 * 
 * Implementa UserDetailsService de Spring Security para cargar administradores
 * desde la base de datos y proporcionarles el rol ADMIN.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Carga un administrador por su nombre de usuario.
     * 
     * @param username Nombre de usuario del administrador
     * @return UserDetails con información del usuario y rol ADMIN
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(admin.getNombre())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }
}
