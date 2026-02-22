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

import java.time.LocalDateTime;
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
import com.store.api.controller.VentaController;
import com.store.api.dto.venta.VentaResponseDTO;
import com.store.api.entity.Producto;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.service.CustomUserDetailsService;
import com.store.api.service.VentaService;

@WebMvcTest(VentaController.class)
@Import(SecurityConfig.class)
public class VentaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private VentaService ventaService;

	@MockitoBean
	private CustomUserDetailsService customUserDetailsService;

	@Test
	@WithMockUser(roles = "ADMIN")
	void GET_findAll_deberiaRetornarListaDeVentas() throws Exception {
		when(ventaService.findAll()).thenReturn(List.of(buildVentaResponseDTO(1L, 2), buildVentaResponseDTO(2L, 1)));

		mockMvc.perform(get("/api/ventas"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(2));

		verify(ventaService, times(1)).findAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void GET_findById_deberiaRetornarVenta() throws Exception {
		when(ventaService.findById(1L)).thenReturn(buildVentaResponseDTO(1L, 2));

		mockMvc.perform(get("/api/ventas/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.cantidad").value(2));

		verify(ventaService, times(1)).findById(1L);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void GET_findById_noEncontrado_deberiaRetornar404() throws Exception {
		when(ventaService.findById(999L)).thenThrow(new ResourceNotFoundException("Venta no encontrada"));

		mockMvc.perform(get("/api/ventas/{id}", 999L))
				.andExpect(status().isNotFound());

		verify(ventaService, times(1)).findById(999L);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void GET_ventasDeHoy_deberiaRetornarListaDTO() throws Exception {
		when(ventaService.ventasDeHoy()).thenReturn(List.of(buildVentaResponseDTO(3L, 1)));

		mockMvc.perform(get("/api/ventas/hoy"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(3));

		verify(ventaService, times(1)).ventasDeHoy();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void GET_ventasDeLaSemana_deberiaRetornarListaDTO() throws Exception {
		when(ventaService.ventasDeLaSemana()).thenReturn(List.of(buildVentaResponseDTO(4L, 5)));

		mockMvc.perform(get("/api/ventas/semana"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(4));

		verify(ventaService, times(1)).ventasDeLaSemana();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void GET_ventasDelMes_deberiaRetornarListaDTO() throws Exception {
		when(ventaService.ventasDelMes()).thenReturn(List.of(buildVentaResponseDTO(5L, 7)));

		mockMvc.perform(get("/api/ventas/mes"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(5));

		verify(ventaService, times(1)).ventasDelMes();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void POST_create_deberiaCrearVenta() throws Exception {
		when(ventaService.create(org.mockito.ArgumentMatchers.any())).thenReturn(buildVentaResponseDTO(6L, 3));

		String requestBody = """
			{
				"cantidad": 3
			}
		""";

		mockMvc.perform(post("/api/ventas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(6))
				.andExpect(jsonPath("$.cantidad").value(3));

		verify(ventaService, times(1)).create(org.mockito.ArgumentMatchers.any());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void PUT_update_deberiaActualizarVenta() throws Exception {
		when(ventaService.update(org.mockito.ArgumentMatchers.eq(7L), org.mockito.ArgumentMatchers.any()))
				.thenReturn(buildVentaResponseDTO(7L, 4));

		String requestBody = """
			{
				"cantidad": 4
			}
		""";

		mockMvc.perform(put("/api/ventas/{id}", 7L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.cantidad").value(4));

		verify(ventaService, times(1)).update(org.mockito.ArgumentMatchers.eq(7L), org.mockito.ArgumentMatchers.any());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void PUT_update_noEncontrado_deberiaRetornar404() throws Exception {
		when(ventaService.update(org.mockito.ArgumentMatchers.eq(999L), org.mockito.ArgumentMatchers.any()))
				.thenThrow(new ResourceNotFoundException("Venta no encontrada"));

		String requestBody = """
			{
				"cantidad": 4
			}
		""";

		mockMvc.perform(put("/api/ventas/{id}", 999L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isNotFound());

		verify(ventaService, times(1)).update(org.mockito.ArgumentMatchers.eq(999L), org.mockito.ArgumentMatchers.any());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void DELETE_delete_deberiaEliminarVenta() throws Exception {
		mockMvc.perform(delete("/api/ventas/{id}", 8L))
				.andExpect(status().isOk());

		verify(ventaService, times(1)).delete(8L);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void DELETE_delete_noEncontrado_deberiaRetornar404() throws Exception {
		doThrow(new ResourceNotFoundException("Venta no encontrada"))
				.when(ventaService).delete(999L);

		mockMvc.perform(delete("/api/ventas/{id}", 999L))
				.andExpect(status().isNotFound());

		verify(ventaService, times(1)).delete(999L);
	}

	private VentaResponseDTO buildVentaResponseDTO(Long id, Integer cantidad) {
		Producto producto = new Producto();
		producto.setId(10L);
		producto.setArticulo("Producto test");
		producto.setPrecioUnitario(50);
		producto.setPrecio(100);
		producto.setStock(30);
		return new VentaResponseDTO(id, producto, cantidad, LocalDateTime.now());
	}
    
}
