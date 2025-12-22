package com.store.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.api.repository.CompaniaRepository;

@ExtendWith(MockitoExtension.class)
public class CompaniaServiceTest {
    
    @Mock
    private CompaniaService companiaService;

    @InjectMocks
    private CompaniaRepository companiaRepository;

    @BeforeEach
    private void setUp() {
        companiaService = new CompaniaService();
    }
}
