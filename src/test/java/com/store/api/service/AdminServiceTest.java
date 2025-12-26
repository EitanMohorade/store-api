package com.store.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.api.entity.Admin;
import com.store.api.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    
    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    private Admin admin;
    @BeforeEach
    private void setUp() {
        admin = new Admin();
        admin.setId(1L);
        admin.setNombre("Admin Test");
        admin.setPassword("123");
    }

    // Test de funcion create
    @Test
    void create_DebeCrearAdminCorrectamente(){

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
