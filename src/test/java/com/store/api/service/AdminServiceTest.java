package com.store.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.api.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    
    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminRepository adminRepository;

    @BeforeEach
    private void setUp() {
        adminService = new AdminService();
    }
}
