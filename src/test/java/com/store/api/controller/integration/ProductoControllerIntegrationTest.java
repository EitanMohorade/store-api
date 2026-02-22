package com.store.api.controller.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.store.api.dto.producto.ProductoResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.service.ProductoService;

/**
 * Test de integración para ProductoController.
 * 
 * Verifica que los endpoints REST funcionan correctamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ProductoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_findAll_testObtenerProductos_admin() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(new ProductoResponseDTO(1L, "A", "desc", 3, 100, null, null, null)));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void GET_findAll_testObtenerProductos_user() throws Exception {
        when(productoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void GET_findAll_testObtenerProductos_sinAutenticacion() throws Exception {
        when(productoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_findById_testObtenerProductoPorId_admin() throws Exception {
        when(productoService.findById(1L)).thenReturn(new ProductoResponseDTO(1L, "A", "desc", 3, 100, null, null, null));

        mockMvc.perform(get("/api/productos/{id}", 1L))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void GET_findById_testObtenerProductoPorId_user() throws Exception {
        when(productoService.findById(1L)).thenReturn(new ProductoResponseDTO(1L, "A", "desc", 3, 100, null, null, null));

        mockMvc.perform(get("/api/productos/{id}", 1L))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void GET_findById_testObtenerProductoPorId_sinAutenticacion() throws Exception {
        when(productoService.findById(1L)).thenReturn(new ProductoResponseDTO(1L, "A", "desc", 3, 100, null, null, null));

        mockMvc.perform(get("/api/productos/{id}", 1L))
                .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void GET_findById_noEncontrado_deberiaRetornar404() throws Exception {
        when(productoService.findById(999L)).thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/{id}", 999L))
            .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void POST_create_admin_deberiaRetornar201() throws Exception {
        when(productoService.create(org.mockito.ArgumentMatchers.any()))
            .thenReturn(new ProductoResponseDTO(2L, "B", "desc", 2, 90, null, null, null));

        String request = """
            {
              "articulo": "B",
              "descripcion": "desc",
              "stock": 2,
              "precio": 90,
              "precioUnitario": 45
            }
            """;

        mockMvc.perform(post("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @WithMockUser(roles = "USER")
        void POST_create_user_deberiaRetornar403() throws Exception {
        String request = """
            {
              "articulo": "B",
              "stock": 2,
              "precio": 90,
              "precioUnitario": 45
            }
            """;

        mockMvc.perform(post("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isForbidden());
        }

        @Test
        void POST_create_sinAutenticacion_deberiaRetornar401() throws Exception {
        String request = """
            {
              "articulo": "B",
              "stock": 2,
              "precio": 90,
              "precioUnitario": 45
            }
            """;

        mockMvc.perform(post("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_admin_deberiaRetornar200() throws Exception {
        when(productoService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
            .thenReturn(new ProductoResponseDTO(1L, "A", "edit", 5, 120, null, null, null));

        String request = """
            {
              "articulo": "A",
              "descripcion": "edit",
              "stock": 5,
              "precio": 120,
              "precioUnitario": 60
            }
            """;

        mockMvc.perform(put("/api/productos/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "USER")
        void PUT_update_user_deberiaRetornar403() throws Exception {
        String request = """
            {
              "articulo": "A",
              "descripcion": "edit",
              "stock": 5,
              "precio": 120,
              "precioUnitario": 60
            }
            """;

        mockMvc.perform(put("/api/productos/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void DELETE_delete_admin_deberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/api/productos/{id}", 1L))
            .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "USER")
        void DELETE_delete_user_deberiaRetornar403() throws Exception {
        mockMvc.perform(delete("/api/productos/{id}", 1L))
            .andExpect(status().isForbidden());
    }
}
