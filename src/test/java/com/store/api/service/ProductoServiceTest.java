package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Producto;
import com.store.api.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test unitario para ProductoService.
 * 
 * Verifica el comportamiento de la lógica de negocio sin depender de la base de datos.
 */
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
