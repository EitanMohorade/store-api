package com.store.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test de integración para CategoriaController.
 * 
 * Verifica que los endpoints REST de categorías funcionan correctamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testObtenerCategorias() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    // Agrega más tests según necesites
}
