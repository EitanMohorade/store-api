package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.store.api.dto.categoria.CategoriaCreateDTO;
import com.store.api.dto.categoria.CategoriaResponseDTO;
import com.store.api.dto.categoria.CategoriaUpdateDTO;
import com.store.api.entity.Categoria;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test unitario para CategoriaService.
 * 
 * Verifica el comportamiento de la lógica de negocio de stock.
 */
@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    @BeforeEach
    public void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria Test");
    }

    // Test de funcion create

    @Test
    void create_DeberiaCrearCategoriaCorrectamente() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre("Categoria Test");

        when(categoriaRepository.save(any())).thenReturn(categoria);

        CategoriaResponseDTO creada = categoriaService.create(dto);

        assertNotNull(creada);
        assertEquals("Categoria Test", creada.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiCategoriaTieneNombreVacio() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre("  ");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoriaService.create(dto);
        });

        assertEquals("El nombre de la categoría no puede estar vacío", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiCategoriaTieneNombreNull() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoriaService.create(dto);
        });

        assertEquals("El nombre de la categoría no puede estar vacío", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiCategoriaTieneNombreMuyLargo() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre("A".repeat(101));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoriaService.create(dto);
        });

        assertEquals("El nombre de la categoría no puede exceder 100 caracteres", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarDuplicateResourceExceptionSiCategoriaYaExiste() {
        CategoriaCreateDTO dto = new CategoriaCreateDTO();
        dto.setNombre("Categoria Test");

        when(categoriaRepository.existsByNombre("Categoria Test")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            categoriaService.create(dto);
        });

        assertEquals("Ya existe una categoría con el mismo nombre", exception.getMessage());
    }

    // Test de funcion delete
    @Test
    void delete_DeberiaEliminarCategoriaExistente() {
        Long categoriaId = 1L;
        when(categoriaRepository.existsById(categoriaId)).thenReturn(true);

        categoriaService.delete(categoriaId);

        verify(categoriaRepository).deleteById(categoriaId);
    }

    @Test
    void delete_DeberiaLanzarResourceNotFoundExceptionSiCategoriaNoExiste() {
        Long categoriaId = 1L;
        when(categoriaRepository.existsById(categoriaId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.delete(categoriaId);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    //Test de funcion findById
    @Test
    void findById_DeberiaRetornarCategoriaExistente() {
        Long categoriaId = 1L;
        when(categoriaRepository.findById(categoriaId)).thenReturn(java.util.Optional.of(categoria));

        CategoriaResponseDTO encontrada = categoriaService.findById(categoriaId);

        assertNotNull(encontrada);
        assertEquals("Categoria Test", encontrada.getNombre());
        verify(categoriaRepository).findById(categoriaId);
    }

    @Test
    void findById_DeberiaLanzarResourceNotFoundExceptionSiCategoriaNoExiste() {
        Long categoriaId = 1L;
        when(categoriaRepository.findById(categoriaId)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.findById(categoriaId);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    //Test de funcion update
    @Test
    void update_DeberiaActualizarCategoriaExistente() {
        Long categoriaId = 1L;
        CategoriaUpdateDTO dto = new CategoriaUpdateDTO();
        dto.setNombre("Categoria Actualizada");
        dto.setDescripcion("Descripcion Actualizada");

        Categoria categoriaGuardada = new Categoria();
        categoriaGuardada.setId(1L);
        categoriaGuardada.setNombre("Categoria Actualizada");
        categoriaGuardada.setDescripcion("Descripcion Actualizada");

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any())).thenReturn(categoriaGuardada);

        CategoriaResponseDTO resultado = categoriaService.update(categoriaId, dto);
        assertNotNull(resultado);
        assertEquals("Categoria Actualizada", resultado.getNombre());
        assertEquals("Descripcion Actualizada", resultado.getDescripcion());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void update_DeberiaLanzarResourceNotFoundExceptionSiCategoriaNoExiste() {
        Long categoriaId = 1L;
        CategoriaUpdateDTO dto = new CategoriaUpdateDTO();
        dto.setNombre("Categoria Actualizada");

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.update(categoriaId, dto);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionSiCategoriaTieneNombreVacio() {
        Long categoriaId = 1L;
        CategoriaUpdateDTO dto = new CategoriaUpdateDTO();
        dto.setNombre("");

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoriaService.update(categoriaId, dto);
        });

        assertEquals("El nombre de la categoría no puede estar vacío", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionSiCategoriaTieneNombreNull() {
        Long categoriaId = 1L;
        CategoriaUpdateDTO dto = new CategoriaUpdateDTO();
        dto.setNombre(null);

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoriaService.update(categoriaId, dto);
        });

        assertEquals("El nombre de la categoría no puede estar vacío", exception.getMessage());
    }

    //Test de funcion findAll
    @Test
    void findAll_DeberiaRetornarListaConUnaCategoria() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> categorias = categoriaService.findAll();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
        verify(categoriaRepository).findAll();
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaCuandoNoHayCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of());

        List<CategoriaResponseDTO> categorias = categoriaService.findAll();

        assertNotNull(categorias);
        assertTrue(categorias.isEmpty());
        verify(categoriaRepository).findAll();
    }

    @Test
    void findAll_DeberiaRetornarListaConVariasCategorias() {
        Categoria otraCategoria = new Categoria();
        otraCategoria.setId(2L);
        otraCategoria.setNombre("Otra Categoria");

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria, otraCategoria));

        List<CategoriaResponseDTO> categorias = categoriaService.findAll();

        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        verify(categoriaRepository).findAll();
    }
}
