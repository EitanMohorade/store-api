package com.store.api.controller.testUnitarios;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.store.api.controller.CategoriaController;
import com.store.api.config.SecurityConfig;
import com.store.api.dto.categoria.CategoriaResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.service.CategoriaService;
import com.store.api.service.CustomUserDetailsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@Import(SecurityConfig.class)
public class CategoriaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void GET_findAll_deberiaDevolverListaDe2Categorias() throws Exception {
        CategoriaResponseDTO dto1 = new CategoriaResponseDTO();
        dto1.setId(1L);
        dto1.setNombre("Cat 1");

        CategoriaResponseDTO dto2 = new CategoriaResponseDTO();
        dto2.setId(2L);
        dto2.setNombre("Cat 2");

        when(categoriaService.findAll()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Cat 1"))
                .andExpect(jsonPath("$[1].nombre").value("Cat 2"));

        verify(categoriaService, times(1)).findAll();
    }

        @Test
        void GET_findById_deberiaDevolverCategoriaConId1() throws Exception {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Cat 1");

        when(categoriaService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/categorias/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Cat 1"));

        verify(categoriaService, times(1)).findById(1L);
        }

        @Test
        void GET_findById_categoriaNoEncontrada_deberiaDevolver404() throws Exception {
        when(categoriaService.findById(999L))
            .thenThrow(new ResourceNotFoundException("Categoría no encontrada"));

        mockMvc.perform(get("/api/categorias/{id}", 999L))
            .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).findById(999L);
        }

        @Test
        void GET_findAll_sinCategorias_deberiaDevolverArrayVacio() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/categorias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));

        verify(categoriaService, times(1)).findAll();
        }

        //Test de POST

        @Test
        @WithMockUser(roles = "ADMIN")
        void POST_create_deberiaCrearCategoriaYDevolver201() throws Exception {
        String nuevaCategoriaJson = """
            {
                "nombre": "Nueva Categoria"
            }
            """;

        CategoriaResponseDTO dtoCreada = new CategoriaResponseDTO();
        dtoCreada.setId(1L);
        dtoCreada.setNombre("Nueva Categoria");

        when(categoriaService.create(org.mockito.ArgumentMatchers.any())).thenReturn(dtoCreada);

        mockMvc.perform(post("/api/categorias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevaCategoriaJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Nueva Categoria"));

        verify(categoriaService, times(1)).create(org.mockito.ArgumentMatchers.any());
        }

        @Test
        void POST_create_sinAutenticacion_deberiaDevolver401() throws Exception {
        String nuevaCategoriaJson = """
            {
                "nombre": "Nueva Categoria"
            }
            """;

        mockMvc.perform(post("/api/categorias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevaCategoriaJson))
            .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void POST_create_nombreVacio_deberiaDevolver422() throws Exception {
        String nuevaCategoriaJson = """
            {
                "nombre": ""
            }
            """;

        when(categoriaService.create(org.mockito.ArgumentMatchers.any()))
            .thenThrow(new ValidationException("El nombre de la categoría no puede estar vacío"));

        mockMvc.perform(post("/api/categorias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevaCategoriaJson))
            .andExpect(status().isUnprocessableEntity());

        verify(categoriaService, times(1)).create(org.mockito.ArgumentMatchers.any());
        }


        //Test de PUT

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_deberiaActualizarCategoriaYDevolver200() throws Exception {
        String categoriaActualizadaJson = """
            {
                "nombre": "Categoria Actualizada",
                "descripcion": "Descripción actualizada"
            }
            """;

        CategoriaResponseDTO dtoActualizada = new CategoriaResponseDTO();
        dtoActualizada.setId(1L);
        dtoActualizada.setNombre("Categoria Actualizada");
        dtoActualizada.setDescripcion("Descripción actualizada");

        when(categoriaService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
            .thenReturn(dtoActualizada);

        mockMvc.perform(put("/api/categorias/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(categoriaActualizadaJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Categoria Actualizada"))
            .andExpect(jsonPath("$.descripcion").value("Descripción actualizada"));

        verify(categoriaService, times(1))
            .update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any());
        }

        @Test
        void PUT_update_sinAutenticacion_deberiaDevolver401() throws Exception {
        String categoriaActualizadaJson = """
            {
                "nombre": "Categoria Actualizada",
                "descripcion": "Descripción actualizada"
            }
            """;

        mockMvc.perform(put("/api/categorias/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(categoriaActualizadaJson))
            .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_categoriaNoEncontrada_deberiaDevolver404() throws Exception {
        String categoriaActualizadaJson = """
            {
                "nombre": "Categoria Actualizada",
                "descripcion": "Descripción actualizada"
            }
            """;

        when(categoriaService.update(org.mockito.ArgumentMatchers.eq(999L), org.mockito.ArgumentMatchers.any()))
            .thenThrow(new ResourceNotFoundException("Categoría no encontrada"));

        mockMvc.perform(put("/api/categorias/{id}", 999L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(categoriaActualizadaJson))
            .andExpect(status().isNotFound());

        verify(categoriaService, times(1))
            .update(org.mockito.ArgumentMatchers.eq(999L), org.mockito.ArgumentMatchers.any());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void PUT_update_nombreVacio_deberiaDevolver422() throws Exception {
        String categoriaActualizadaJson = """
            {
                "nombre": "",
                "descripcion": "Descripción actualizada"
            }
            """;

        when(categoriaService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
            .thenThrow(new ValidationException("El nombre de la categoría no puede estar vacío"));

        mockMvc.perform(put("/api/categorias/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(categoriaActualizadaJson))
            .andExpect(status().isUnprocessableEntity());

        verify(categoriaService, times(1))
            .update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any());
        }

        //Test de DELETE

        @Test
        @WithMockUser(roles = "ADMIN")
        void DELETE_delete_deberiaEliminarCategoriaYDevolver204() throws Exception {
        mockMvc.perform(delete("/api/categorias/{id}", 1L))
            .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).delete(1L);
        }

        @Test
        void DELETE_delete_sinAutenticacion_deberiaDevolver401() throws Exception {
        mockMvc.perform(delete("/api/categorias/{id}", 1L))
            .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void DELETE_delete_categoriaNoEncontrada_deberiaDevolver404() throws Exception {
        doThrow(new ResourceNotFoundException("Categoría no encontrada"))
            .when(categoriaService).delete(999L);

        mockMvc.perform(delete("/api/categorias/{id}", 999L))
            .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).delete(999L);
        }
}
