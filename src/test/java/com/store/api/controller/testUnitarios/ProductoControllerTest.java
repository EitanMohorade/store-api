package com.store.api.controller.testUnitarios;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.store.api.config.SecurityConfig;
import com.store.api.controller.ProductoController;
import com.store.api.dto.producto.ProductoResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.service.CustomUserDetailsService;
import com.store.api.service.ProductoService;

@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_list_admin_deberiaDevolverProductos() throws Exception {
	when(productoService.findAll()).thenReturn(List.of(new ProductoResponseDTO(1L, "A", "desc", 5, 100, null, null, null)));

	mockMvc.perform(get("/api/productos"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()").value(1));

	verify(productoService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void GET_list_user_deberiaDevolverProductos() throws Exception {
	when(productoService.findAll()).thenReturn(List.of());

	mockMvc.perform(get("/api/productos"))
		.andExpect(status().isOk());

	verify(productoService, times(1)).findAll();
    }

    @Test
	void GET_list_sinAutenticacion_deberiaPermitirComoUserPorDefecto() throws Exception {
		when(productoService.findAll()).thenReturn(List.of());

	mockMvc.perform(get("/api/productos"))
				.andExpect(status().isOk());

		verify(productoService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_create_admin_deberiaCrearYDevolver201() throws Exception {
	when(productoService.create(org.mockito.ArgumentMatchers.any()))
		.thenReturn(new ProductoResponseDTO(1L, "A", "desc", 5, 100, null, null, null));

	String body = """
		{
		    "articulo": "A",
		    "descripcion": "desc",
		    "stock": 5,
		    "precio": 100,
		    "precioUnitario": 50
		}
		""";

	mockMvc.perform(post("/api/productos")
		.contentType(MediaType.APPLICATION_JSON)
		.content(body))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(1));

	verify(productoService, times(1)).create(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void POST_create_user_deberiaDevolver403() throws Exception {
	String body = """
		{
		    "articulo": "A",
		    "stock": 5,
		    "precio": 100,
		    "precioUnitario": 50
		}
		""";

	mockMvc.perform(post("/api/productos")
		.contentType(MediaType.APPLICATION_JSON)
		.content(body))
		.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_update_admin_deberiaActualizar() throws Exception {
	when(productoService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
		.thenReturn(new ProductoResponseDTO(1L, "A", "edit", 3, 120, null, null, null));

	String body = """
		{
		    "articulo": "A",
		    "descripcion": "edit",
		    "stock": 3,
		    "precio": 120,
		    "precioUnitario": 60
		}
		""";

	mockMvc.perform(put("/api/productos/{id}", 1L)
		.contentType(MediaType.APPLICATION_JSON)
		.content(body))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.precio").value(120));

	verify(productoService, times(1)).update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_admin_deberiaRetornar204() throws Exception {
	mockMvc.perform(delete("/api/productos/{id}", 1L))
		.andExpect(status().isNoContent());

	verify(productoService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_delete_noEncontrado_deberiaRetornar404() throws Exception {
	doThrow(new ResourceNotFoundException("Producto no encontrado"))
		.when(productoService).delete(99L);

	mockMvc.perform(delete("/api/productos/{id}", 99L))
		.andExpect(status().isNotFound());

	verify(productoService, times(1)).delete(99L);
    }
    
}
