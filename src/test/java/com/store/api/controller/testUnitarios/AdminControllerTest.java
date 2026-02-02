package com.store.api.controller.testUnitarios;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.store.api.config.SecurityConfig;
import com.store.api.controller.AdminController;
import com.store.api.dto.admin.AdminResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.service.AdminService;
import com.store.api.service.CustomUserDetailsService;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // Test de POST

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_deberiaCrearAdminYDevolver201() throws Exception {
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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("admin_test"));

        verify(adminService, times(1)).create(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void POST_create_sinAutenticacion_deberiaDevolver401() throws Exception {
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

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_nombreVacio_deberiaDevolver422() throws Exception {
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

        verify(adminService, times(1)).create(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_passwordVacio_deberiaDevolver422() throws Exception {
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

        verify(adminService, times(1)).create(org.mockito.ArgumentMatchers.any());
    }

    // Test de PUT

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_deberiaActualizarAdminYDevolver200() throws Exception {
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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("admin_actualizado"));

        verify(adminService, times(1))
                .update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void PUT_update_sinAutenticacion_deberiaDevolver401() throws Exception {
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
    void PUT_update_adminNoEncontrado_deberiaDevolver404() throws Exception {
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

        verify(adminService, times(1))
                .update(org.mockito.ArgumentMatchers.eq(999L), org.mockito.ArgumentMatchers.any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_nombreVacio_deberiaDevolver422() throws Exception {
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

        verify(adminService, times(1))
                .update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any());
    }

    // Test de DELETE

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_deberiaEliminarAdminYDevolver204() throws Exception {
        mockMvc.perform(delete("/api/admins/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(adminService, times(1)).delete(1L);
    }

    @Test
    void DELETE_delete_sinAutenticacion_deberiaDevolver401() throws Exception {
        mockMvc.perform(delete("/api/admins/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_adminNoEncontrado_deberiaDevolver404() throws Exception {
        doThrow(new ResourceNotFoundException("Administrador no encontrado"))
                .when(adminService).delete(999L);

        mockMvc.perform(delete("/api/admins/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).delete(999L);
    }
}
