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

import com.store.api.controller.CompaniaController;
import com.store.api.config.SecurityConfig;
import com.store.api.dto.compania.CompaniaResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.service.CompaniaService;
import com.store.api.service.CustomUserDetailsService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CompaniaController.class)
@Import(SecurityConfig.class)
public class CompaniaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompaniaService companiaService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // Test de GET

    @Test
    void GET_findAll_deberiaDevolverListaDe2Companias() throws Exception {
	CompaniaResponseDTO dto1 = new CompaniaResponseDTO();
	dto1.setId(1L);
	dto1.setNombre("Compañía 1");

	CompaniaResponseDTO dto2 = new CompaniaResponseDTO();
	dto2.setId(2L);
	dto2.setNombre("Compañía 2");

	when(companiaService.findAll()).thenReturn(List.of(dto1, dto2));

	mockMvc.perform(get("/api/companias"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(2))
		.andExpect(jsonPath("$[0].nombre").value("Compañía 1"))
		.andExpect(jsonPath("$[1].nombre").value("Compañía 2"));

	verify(companiaService, times(1)).findAll();
    }

    @Test
    void GET_findById_deberiaDevolverCompaniaConId1() throws Exception {
	CompaniaResponseDTO dto = new CompaniaResponseDTO();
	dto.setId(1L);
	dto.setNombre("Compañía 1");

	when(companiaService.findById(1L)).thenReturn(dto);

	mockMvc.perform(get("/api/companias/{id}", 1L))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nombre").value("Compañía 1"));

	verify(companiaService, times(1)).findById(1L);
    }

    @Test
    void GET_findById_companiaNoEncontrada_deberiaDevolver404() throws Exception {
	when(companiaService.findById(999L))
		.thenThrow(new ResourceNotFoundException("Compañía no encontrada"));

	mockMvc.perform(get("/api/companias/{id}", 999L))
		.andExpect(status().isNotFound());

	verify(companiaService, times(1)).findById(999L);
    }

    @Test
    void GET_findAll_sinCompanias_deberiaDevolverArrayVacio() throws Exception {
	when(companiaService.findAll()).thenReturn(List.of());

	mockMvc.perform(get("/api/companias"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isArray())
		.andExpect(jsonPath("$.length()").value(0));

	verify(companiaService, times(1)).findAll();
    }

    // Test de POST

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_deberiaCrearCompaniaYDevolver201() throws Exception {
	String nuevaCompaniaJson = """
		{
		    "nombre": "Compañía Nueva"
		}
		""";

	CompaniaResponseDTO dtoCreada = new CompaniaResponseDTO();
	dtoCreada.setId(1L);
	dtoCreada.setNombre("Compañía Nueva");

	when(companiaService.create(any())).thenReturn(dtoCreada);

	mockMvc.perform(post("/api/companias")
		.contentType(MediaType.APPLICATION_JSON)
		.content(nuevaCompaniaJson))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nombre").value("Compañía Nueva"));

	verify(companiaService, times(1)).create(any());
    }

    @Test
    void POST_create_sinAutenticacion_deberiaDevolver401() throws Exception {
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

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_nombreVacio_deberiaDevolver422() throws Exception {
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

	verify(companiaService, times(1)).create(any());
    }

    // Test de PUT

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_deberiaActualizarCompaniaYDevolver200() throws Exception {
	String companiaActualizadaJson = """
		{
		    "nombre": "Compañía Actualizada"
		}
		""";

	CompaniaResponseDTO dtoActualizada = new CompaniaResponseDTO();
	dtoActualizada.setId(1L);
	dtoActualizada.setNombre("Compañía Actualizada");

	when(companiaService.update(eq(1L), any()))
		.thenReturn(dtoActualizada);

	mockMvc.perform(put("/api/companias/{id}", 1L)
		.contentType(MediaType.APPLICATION_JSON)
		.content(companiaActualizadaJson))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nombre").value("Compañía Actualizada"));

	verify(companiaService, times(1))
		.update(eq(1L), any());
    }

    @Test
    void PUT_update_sinAutenticacion_deberiaDevolver401() throws Exception {
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
    void PUT_update_companiaNoEncontrada_deberiaDevolver404() throws Exception {
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

	verify(companiaService, times(1))
		.update(eq(999L), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_nombreVacio_deberiaDevolver422() throws Exception {
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

	verify(companiaService, times(1))
		.update(eq(1L), any());
    }

    // Test de DELETE

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_deberiaEliminarCompaniaYDevolver204() throws Exception {
	mockMvc.perform(delete("/api/companias/{id}", 1L))
		.andExpect(status().isNoContent());

	verify(companiaService, times(1)).delete(1L);
    }

    @Test
    void DELETE_delete_sinAutenticacion_deberiaDevolver401() throws Exception {
	mockMvc.perform(delete("/api/companias/{id}", 1L))
		.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_companiaNoEncontrada_deberiaDevolver404() throws Exception {
	doThrow(new ResourceNotFoundException("Compañía no encontrada"))
		.when(companiaService).delete(999L);

	mockMvc.perform(delete("/api/companias/{id}", 999L))
		.andExpect(status().isNotFound());

	verify(companiaService, times(1)).delete(999L);
    }
}
