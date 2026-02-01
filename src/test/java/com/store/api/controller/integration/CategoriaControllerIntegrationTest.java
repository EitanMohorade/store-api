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

import com.store.api.dto.categoria.CategoriaResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.service.CategoriaService;

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

    @MockitoBean
    private CategoriaService categoriaService;


    //Test de GET

    @Test
    void GET_findAll_DeberiaListar3Categorias() throws Exception {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        CategoriaResponseDTO dto2 = new CategoriaResponseDTO();
        CategoriaResponseDTO dto3 = new CategoriaResponseDTO();
        dto2.setId(2L);
        dto2.setNombre("Test2");
        dto3.setId(3L);
        dto3.setNombre("Test3");
        dto.setId(1L);
        dto.setNombre("Test");

        when(categoriaService.findAll()).thenReturn(List.of(dto, dto2, dto3));
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nombre").value("Test"))
                .andExpect(jsonPath("$[1].nombre").value("Test2"))
                .andExpect(jsonPath("$[2].nombre").value("Test3"));
    }

    @Test
    void GET_findById_DeberiaDevolverCategoriaConId1() throws Exception {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Test");

        when(categoriaService.findById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/categorias/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test"));
    }

    @Test
    void GET_findById_CategoriaNoEncontrada_DeberiaDevolver404() throws Exception {
        when(categoriaService.findById(999L)).thenThrow(new ResourceNotFoundException("Categoría no encontrada"));

        mockMvc.perform(get("/api/categorias/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void GET_findAll_SinCategorias_DeberiaDevolverArrayVacio() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    //Test de POST

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_DeberiaCrearCategoriaConSoloNombreYDevolver201() throws Exception {
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nueva Categoria"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_NombreDeCategoriaVacio_DeberiaDevolver422() throws Exception {
        String nuevaCategoriaJson = """
                {
                    "nombre": ""
                }
                """;

        CategoriaResponseDTO dtoCreada = new CategoriaResponseDTO();
        dtoCreada.setId(1L);
        dtoCreada.setNombre("");

        when(categoriaService.create(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ValidationException("El nombre de la categoría no puede estar vacío"));

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevaCategoriaJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_DeberiaCrearCategoriaConNombreYDescripcion_DeberiaDevolver201() throws Exception {
        String nuevaCategoriaJson = """
                {
                    "nombre": "Nueva Categoria",
                    "descripcion": "Descripción de la nueva categoría"
                }
                """;

        CategoriaResponseDTO dtoCreada = new CategoriaResponseDTO();
        dtoCreada.setId(1L);
        dtoCreada.setNombre("Nueva Categoria");
        dtoCreada.setDescripcion("Descripción de la nueva categoría");

        when(categoriaService.create(org.mockito.ArgumentMatchers.any())).thenReturn(dtoCreada);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevaCategoriaJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nueva Categoria"))
                .andExpect(jsonPath("$.descripcion").value("Descripción de la nueva categoría"));
    }

    @Test
    void POST_create_SinAutenticacion_DeberiaDevolver401() throws Exception {
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

    //Test de PUT

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_DeberiaActualizarNombreYDescripcionDeCategoriaYDevolver200() throws Exception {
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Categoria Actualizada"))
                .andExpect(jsonPath("$.descripcion").value("Descripción actualizada"));
    }

    @Test
    void PUT_update_SinAutenticacion_DeberiaDevolver401() throws Exception {
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
    void PUT_update_CategoriaNoEncontrada_DeberiaDevolver404() throws Exception {
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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_NombreDeCategoriaVacio_DeberiaDevolver422() throws Exception {
        String categoriaActualizadaJson = """
                {
                    "nombre": "",
                    "descripcion": "Descripción actualizada"
                }
                """;

        CategoriaResponseDTO dtoActualizada = new CategoriaResponseDTO();
        dtoActualizada.setId(1L);
        dtoActualizada.setNombre("");
        dtoActualizada.setDescripcion("Descripción actualizada");

        when(categoriaService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ValidationException("El nombre de la categoría no puede estar vacío"));
        
            mockMvc.perform(put("/api/categorias/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaActualizadaJson))
                .andExpect(status().isUnprocessableEntity());
    }

    //Test de DELETE

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_DeberiaEliminarCategoriaYDevolver204() throws Exception {
        mockMvc.perform(delete("/api/categorias/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETE_delete_SinAutenticacion_DeberiaDevolver401() throws Exception {
        mockMvc.perform(delete("/api/categorias/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_CategoriaNoEncontrada_DeberiaDevolver404() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Categoría no encontrada"))
                .when(categoriaService).delete(999L);

        mockMvc.perform(delete("/api/categorias/{id}", 999L))
                .andExpect(status().isNotFound());
    }

}
