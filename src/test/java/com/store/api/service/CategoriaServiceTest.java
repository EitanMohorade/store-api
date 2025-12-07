package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.store.api.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test unitario para CategoriaService.
 * 
 * Verifica el comportamiento de la lógica de negocio de stock.
 */
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository CategoriaRepository;

    @InjectMocks
    private CategoriaService CategoriaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
