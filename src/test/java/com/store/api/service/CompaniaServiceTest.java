package com.store.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.api.entity.Compania;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.CompaniaRepository;

/**
 * Pruebas unitarias para el servicio CompaniaService.
 * 
 * Estas pruebas verifican la lógica de negocio y las validaciones
 * implementadas en el servicio de compañías.
 * 
 */
@ExtendWith(MockitoExtension.class)
public class CompaniaServiceTest {
    
    @InjectMocks
    private CompaniaService companiaService;

    @Mock
    private CompaniaRepository companiaRepository;

    private Compania compania;
    @BeforeEach
    private void setUp() {
        compania = new Compania();
        compania.setId(1L);
        compania.setNombre("Compania Test");
    }


    //Test de funcion create

    @Test
   void create_DeberiaCrearCompaniaCuandoDatosSonValidos() {
        
        when(companiaRepository.save(any()))
            .thenAnswer(invocation -> {
                Compania c = invocation.getArgument(0);
                c.setId(1L);
                return c;
            });

        Compania nuevaCompania = companiaService.create(compania);

        assertNotNull(nuevaCompania.getId());
        assertEquals("Compania Test", nuevaCompania.getNombre());
   }

   @Test
    void create_DeberiaLanzarValidationExceptionCuandoNombreEsNull() {
          Compania companiaInvalida = new Compania();
          companiaInvalida.setNombre(null);

          ValidationException exception = assertThrows(ValidationException.class, () -> {
              companiaService.create(companiaInvalida);
          });

          assertEquals("El nombre de la compañía es obligatorio", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoNombreExcedeLongitudMaxima() {
          Compania companiaInvalida = new Compania();
          companiaInvalida.setNombre("A".repeat(101));

          ValidationException exception = assertThrows(ValidationException.class, () -> {
              companiaService.create(companiaInvalida);
          });

          assertEquals("El nombre de la compañía no puede exceder 100 caracteres", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoNombreYaExiste() {
          Compania companiaInvalida = new Compania();
          companiaInvalida.setNombre("Compania Test");

          when(companiaRepository.existsByNombre("Compania Test")).thenReturn(true);

          ValidationException exception = assertThrows(ValidationException.class, () -> {
              companiaService.create(companiaInvalida);
          });

          assertEquals("Ya existe una compañía con el mismo nombre", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoNombreEsVacio() {
          Compania companiaInvalida = new Compania();
          companiaInvalida.setNombre("   ");

          ValidationException exception = assertThrows(ValidationException.class, () -> {
              companiaService.create(companiaInvalida);
          });

          assertEquals("El nombre de la compañía es obligatorio", exception.getMessage());
    }

    //Test de funcion delete

    @Test
    void delete_DeberiaEliminarCompaniaCuandoIdExiste() {
        when(companiaRepository.existsById(1L)).thenReturn(true);
        companiaService.delete(1L);

        verify(companiaRepository).deleteById(1L);
    }
    
    @Test
    void delete_DeberiaLanzarResourceNotFoundExceptionCuandoIdNoExiste() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            companiaService.delete(999L);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    //Test de funcion update
    @Test
    void update_DeberiaActualizarCompaniaCuandoDatosSonValidos() {
        when(companiaRepository.findById(1L))
            .thenReturn(Optional.of(compania));

        when(companiaRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        Compania companiaActualizada = new Compania();
        companiaActualizada.setNombre("Compania Actualizada");

        Compania resultado = companiaService.update(1L, companiaActualizada);

        assertEquals("Compania Actualizada", resultado.getNombre());
    }

    @Test
    void update_DeberiaLanzarResourceNotFoundExceptionCuandoIdNoExiste() {
        Compania companiaActualizada = new Compania();
        companiaActualizada.setNombre("Compania Actualizada");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            companiaService.update(999L, companiaActualizada);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }
    @Test
    void update_DeberiaLanzarValidationExceptionCuandoNombreEsNull() {
        when(companiaRepository.findById(1L))
            .thenReturn(Optional.of(compania));

        Compania companiaInvalida = new Compania();
        companiaInvalida.setNombre(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            companiaService.update(1L, companiaInvalida);
        });

        assertEquals("El nombre de la compañía es obligatorio", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionCuandoNombreExcedeLongitudMaxima() {
        when(companiaRepository.findById(1L))
            .thenReturn(Optional.of(compania));

        Compania companiaInvalida = new Compania();
        companiaInvalida.setNombre("A".repeat(101));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            companiaService.update(1L, companiaInvalida);
        });

        assertEquals("El nombre de la compañía no puede exceder 100 caracteres", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionCuandoNombreYaExiste() {
        when(companiaRepository.findById(1L))
            .thenReturn(Optional.of(compania));

        Compania companiaInvalida = new Compania();
        companiaInvalida.setNombre("Compania Test");

        when(companiaRepository.existsByNombre("Compania Test")).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            companiaService.update(1L, companiaInvalida);
        });

        assertEquals("Ya existe una compañía con el mismo nombre", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionCuandoNombreEsVacio() {
        when(companiaRepository.findById(1L))
            .thenReturn(Optional.of(compania));

        Compania companiaInvalida = new Compania();
        companiaInvalida.setNombre("   ");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            companiaService.update(1L, companiaInvalida);
        });

        assertEquals("El nombre de la compañía es obligatorio", exception.getMessage());
    }
    //Test de funcion findById
    @Test
    void findById_DeberiaRetornarCompaniaCuandoIdExiste() {
        when(companiaRepository.findById(1L))
            .thenReturn(Optional.of(compania));

        Compania resultado = companiaService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Compania Test", resultado.getNombre());
    }

    @Test
    void findById_DeberiaLanzarResourceNotFoundExceptionCuandoIdNoExiste() {
        when(companiaRepository.findById(999L))
            .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            companiaService.findById(999L);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    //Test de funcion findAll
    @Test
    void findAll_DeberiaRetornarListaDe3Companias() {
        
        Compania segundaCompania = new Compania();
        segundaCompania.setId(2L);
        segundaCompania.setNombre("Segunda Compania");

        Compania terceraCompania = new Compania();
        terceraCompania.setId(3L);
        terceraCompania.setNombre("Tercera Compania");
        
        when(companiaRepository.findAll())
            .thenReturn(List.of(compania, segundaCompania, terceraCompania));

        List<Compania> resultado = companiaService.findAll();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Compania Test", resultado.get(0).getNombre());
        assertEquals("Segunda Compania", resultado.get(1).getNombre());
        assertEquals("Tercera Compania", resultado.get(2).getNombre());
    }

    //Test de funcion existsById

    @Test
    void existsById_DeberiaRetornarTrueCuandoIdExiste() {
        when(companiaRepository.existsById(1L)).thenReturn(true);

        boolean existe = companiaService.existsById(1L);

        assertEquals(true, existe);
    }

    @Test
    void existsById_DeberiaRetornarFalseCuandoIdNoExiste() {
        when(companiaRepository.existsById(999L)).thenReturn(false);

        boolean existe = companiaService.existsById(999L);

        assertEquals(false, existe);
    }

    //Test de funcion findByNombre

    @Test
    void findByNombre_DeberiaRetornarCompaniaCuandoNombreExiste() {
        when(companiaRepository.findByNombreIgnoreCase("Compania Test"))
            .thenReturn(Optional.of(compania));

        Compania resultado = companiaService.findByNombre("Compania Test");

        assertNotNull(resultado);
        assertEquals("Compania Test", resultado.getNombre());
    }
    @Test
    void findByNombre_DeberiaLanzarResourceNotFoundExceptionCuandoNombreNoExiste() {
        when(companiaRepository.findByNombreIgnoreCase("Compania Inexistente"))
            .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            companiaService.findByNombre("Compania Inexistente");
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    //Test de funcion count
    @Test
    void count_DeberiaRetornarNumeroTotalDeCompanias() {
        when(companiaRepository.count()).thenReturn(5L);

        Long total = companiaService.count();

        assertEquals(5L, total);
    }
    
    @Test
    void count_DeberiaRetornarCeroCuandoNoHayCompanias() {
        when(companiaRepository.count()).thenReturn(0L);

        Long total = companiaService.count();

        assertEquals(0L, total);
    }
}
