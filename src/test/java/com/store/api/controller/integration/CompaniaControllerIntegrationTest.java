package com.store.api.controller.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

import com.store.api.dto.categoria.CategoriaResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.service.CategoriaService;

import com.store.api.dto.compania.CompaniaResponseDTO;
import com.store.api.service.CompaniaService;

/**
 * Test de integración para CompaniaController.
 * 
 * Verifica que los endpoints REST de compañías funcionan correctamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CompaniaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompaniaService companiaService;


    //Test de GET

    @Test
    void GET_findAll_DeberiaListar3Companias() throws Exception {
        CompaniaResponseDTO compania1 = new CompaniaResponseDTO();
        compania1.setId(1L);
        compania1.setNombre("Compañía A");
        CompaniaResponseDTO compania2 = new CompaniaResponseDTO();
        compania2.setId(2L);
        compania2.setNombre("Compañía B");
        CompaniaResponseDTO compania3 = new CompaniaResponseDTO();
        compania3.setId(3L);
        compania3.setNombre("Compañía C");

        when(companiaService.findAll()).thenReturn(List.of(compania1, compania2, compania3));


        mockMvc.perform(get("/api/companias"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].nombre").value("Compañía A"))
            .andExpect(jsonPath("$[1].nombre").value("Compañía B"))
            .andExpect(jsonPath("$[2].nombre").value("Compañía C"));
    }

        @Test
        void GET_findById_DeberiaDevolverCompaniaConId1() throws Exception {
        CompaniaResponseDTO compania = new CompaniaResponseDTO();
        compania.setId(1L);
        compania.setNombre("Compañía A");

        when(companiaService.findById(1L)).thenReturn(compania);

        mockMvc.perform(get("/api/companias/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Compañía A"));
        }

        @Test
        void GET_findById_CompaniaNoEncontrada_DeberiaDevolver404() throws Exception {
        when(companiaService.findById(999L)).thenThrow(new ResourceNotFoundException("Compañía no encontrada"));

        mockMvc.perform(get("/api/companias/{id}", 999L))
            .andExpect(status().isNotFound());
        }

        @Test
        void GET_findAll_SinCompanias_DeberiaDevolverArrayVacio() throws Exception {
        when(companiaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/companias"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
        }

        //Test de POST

        @Test
        @WithMockUser(roles = "ADMIN")
        void POST_create_DeberiaCrearCompaniaYDevolver201() throws Exception {
        String nuevaCompaniaJson = """
            {
                "nombre": "Compañía Nueva"
            }
            """;

        CompaniaResponseDTO companiaCreada = new CompaniaResponseDTO();
        companiaCreada.setId(1L);
        companiaCreada.setNombre("Compañía Nueva");

        when(companiaService.create(org.mockito.ArgumentMatchers.any())).thenReturn(companiaCreada);

        mockMvc.perform(post("/api/companias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevaCompaniaJson))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Compañía Nueva"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void POST_create_NombreVacio_DeberiaDevolver422() throws Exception {
        String nuevaCompaniaJson = """
            {
                "nombre": ""
            }
            """;

        when(companiaService.create(any()))
            .thenThrow(new ValidationException("El nombre de la compañía es obligatorio"));

        mockMvc.perform(post("/api/companias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevaCompaniaJson))
            .andExpect(status().isUnprocessableEntity());
        }

        @Test
        void POST_create_SinAutenticacion_DeberiaDevolver401() throws Exception {
        String nuevaCompaniaJson = """
            {
                "nombre": "Compañía Nueva"
            }
            """;

        mockMvc.perform(post("/api/companias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevaCompaniaJson))
            .andExpect(status().isUnauthorized());
        }

        //Test de PUT

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_DeberiaActualizarCompaniaYDevolver200() throws Exception {
        String companiaActualizadaJson = """
            {
                "nombre": "Compañía Actualizada"
            }
            """;

        CompaniaResponseDTO companiaActualizada = new CompaniaResponseDTO();
        companiaActualizada.setId(1L);
        companiaActualizada.setNombre("Compañía Actualizada");

        when(companiaService.update(eq(1L), any()))
            .thenReturn(companiaActualizada);

        mockMvc.perform(put("/api/companias/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(companiaActualizadaJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Compañía Actualizada"));
        }

        @Test
        void PUT_update_SinAutenticacion_DeberiaDevolver401() throws Exception {
        String companiaActualizadaJson = """
            {
                "nombre": "Compañía Actualizada"
            }
            """;

        mockMvc.perform(put("/api/companias/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(companiaActualizadaJson))
            .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_CompaniaNoEncontrada_DeberiaDevolver404() throws Exception {
        String companiaActualizadaJson = """
            {
                "nombre": "Compañía Actualizada"
            }
            """;

        when(companiaService.update(eq(999L), any()))
            .thenThrow(new ResourceNotFoundException("Compañía no encontrada"));

        mockMvc.perform(put("/api/companias/{id}", 999L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(companiaActualizadaJson))
            .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_NombreVacio_DeberiaDevolver422() throws Exception {
        String companiaActualizadaJson = """
            {
                "nombre": ""
            }
            """;

        when(companiaService.update(eq(1L), any()))
            .thenThrow(new ValidationException("El nombre de la compañía es obligatorio"));

        mockMvc.perform(put("/api/companias/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(companiaActualizadaJson))
            .andExpect(status().isUnprocessableEntity());
        }

        //Test de DELETE

        @Test
        @WithMockUser(roles = "ADMIN")
        void DELETE_delete_DeberiaEliminarCompaniaYDevolver204() throws Exception {
        mockMvc.perform(delete("/api/companias/{id}", 1L))
            .andExpect(status().isNoContent());
        }

        @Test
        void DELETE_delete_SinAutenticacion_DeberiaDevolver401() throws Exception {
        mockMvc.perform(delete("/api/companias/{id}", 1L))
            .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void DELETE_delete_CompaniaNoEncontrada_DeberiaDevolver404() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Compañía no encontrada"))
            .when(companiaService).delete(999L);

        mockMvc.perform(delete("/api/companias/{id}", 999L))
            .andExpect(status().isNotFound());
        }

    
}
