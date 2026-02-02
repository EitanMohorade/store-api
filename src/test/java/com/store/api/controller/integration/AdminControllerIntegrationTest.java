package com.store.api.controller.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.store.api.dto.admin.AdminResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.service.AdminService;

/**
 * Test de integración para AdminController.
 * 
 * Verifica que los endpoints REST de administradores funcionan correctamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    //Test de POST

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_DeberiaCrearAdminYDevolver201() throws Exception {
        String nuevoAdminJson = """
                {
                    "nombre": "admin_test",
                    "password": "password123"
                }
                """;

        AdminResponseDTO adminCreado = new AdminResponseDTO(1L, "admin_test");

        when(adminService.create(org.mockito.ArgumentMatchers.any())).thenReturn(adminCreado);

        mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevoAdminJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("admin_test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_NombreVacio_DeberiaDevolver422() throws Exception {
        String nuevoAdminJson = """
                {
                    "nombre": "",
                    "password": "password123"
                }
                """;

        when(adminService.create(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ValidationException("El nombre del administrador no puede estar vacío"));

        mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevoAdminJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_PasswordVacio_DeberiaDevolver422() throws Exception {
        String nuevoAdminJson = """
                {
                    "nombre": "admin_test",
                    "password": ""
                }
                """;

        when(adminService.create(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ValidationException("La contraseña del administrador no puede estar vacía"));

        mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevoAdminJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void POST_create_SinAutenticacion_DeberiaDevolver401() throws Exception {
        String nuevoAdminJson = """
                {
                    "nombre": "admin_test",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevoAdminJson))
                .andExpect(status().isUnauthorized());
    }

    //Test de PUT

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_DeberiaActualizarAdminYDevolver200() throws Exception {
        String adminActualizadoJson = """
                {
                    "nombre": "admin_actualizado",
                    "password": "newpassword123"
                }
                """;

        AdminResponseDTO adminActualizado = new AdminResponseDTO(1L, "admin_actualizado");

        when(adminService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
                .thenReturn(adminActualizado);

        mockMvc.perform(put("/api/admins/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminActualizadoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("admin_actualizado"));
    }

    @Test
    void PUT_update_SinAutenticacion_DeberiaDevolver401() throws Exception {
        String adminActualizadoJson = """
                {
                    "nombre": "admin_actualizado",
                    "password": "newpassword123"
                }
                """;

        mockMvc.perform(put("/api/admins/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminActualizadoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_AdminNoEncontrado_DeberiaDevolver404() throws Exception {
        String adminActualizadoJson = """
                {
                    "nombre": "admin_actualizado",
                    "password": "newpassword123"
                }
                """;

        when(adminService.update(org.mockito.ArgumentMatchers.eq(999L), org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ResourceNotFoundException("Administrador no encontrado"));

        mockMvc.perform(put("/api/admins/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminActualizadoJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_NombreVacio_DeberiaDevolver422() throws Exception {
        String adminActualizadoJson = """
                {
                    "nombre": "",
                    "password": "newpassword123"
                }
                """;

        when(adminService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ValidationException("El nombre del administrador no puede estar vacío"));

        mockMvc.perform(put("/api/admins/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminActualizadoJson))
                .andExpect(status().isUnprocessableEntity());
    }

    //Test de DELETE

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_DeberiaEliminarAdminYDevolver204() throws Exception {
        mockMvc.perform(delete("/api/admins/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETE_delete_SinAutenticacion_DeberiaDevolver401() throws Exception {
        mockMvc.perform(delete("/api/admins/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_AdminNoEncontrado_DeberiaDevolver404() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Administrador no encontrado"))
                .when(adminService).delete(999L);

        mockMvc.perform(delete("/api/admins/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
