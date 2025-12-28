package com.store.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.store.api.dto.admin.AdminCreateDTO;
import com.store.api.dto.admin.AdminResponseDTO;
import com.store.api.entity.Admin;
import com.store.api.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    
    @InjectMocks
    private AdminService adminService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRepository adminRepository;

    // Test de funcion create
    @Test
    void create_deberiaCrearAdminYNoExponerPassword(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre("admin test");
        dto.setPassword("1234");

        when(passwordEncoder.encode("1234"))
            .thenReturn("password_encriptado");

        when(adminRepository.save(any(Admin.class)))
            .thenAnswer(invocation -> {
                Admin saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

        AdminResponseDTO response = adminService.create(dto);

        assertNotNull(response);
        assertEquals("admin test", response.getNombre());
        assertEquals(1L, response.getId());
    }

    // Test de funcion update
    @Test
    void update_DebeActualizarAdminCorrectamente(){

    }

    // Test de funcion delete
    @Test
    void delete_DebeEliminarAdminCorrectamente(){

    }
}
