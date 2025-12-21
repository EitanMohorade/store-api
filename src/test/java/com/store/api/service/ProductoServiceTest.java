package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


import com.store.api.entity.Producto;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.StockInsufficientException;
import com.store.api.exception.ValidationException;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test unitario para ProductoService.
 * 
 * Verifica el comportamiento de la lógica de negocio sin depender de la base de datos.
 */
@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoExistente;
    private Producto productoExistente2;

    @BeforeEach
    public void setUp() {
        productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setArticulo("prueba 1");
        productoExistente.setPrecio(1200);
        productoExistente.setStock(15);
        productoExistente.setDescripcion("description prueba 1");
        productoExistente.setCategoria(null);

        productoExistente2 = new Producto();
        productoExistente2.setId(2L);
        productoExistente2.setArticulo("prueba 2");
        productoExistente2.setPrecio(99);
        productoExistente2.setStock(50);
        productoExistente2.setDescripcion("description prueba 2");
        productoExistente2.setCategoria(null); 
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiElArticuloEsNulo() {
        Producto producto = new Producto();
        producto.setArticulo(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.create(producto);
        });

        assertEquals(
            "El artículo no puede estar vacío",
            exception.getMessage()
        );
    }

    @Test
    void update_DeberiaLanzarValidationExceptionSiElPrecioEsNegativo() {

        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        Producto producto = new Producto();
        producto.setArticulo("Producto de prueba");
        producto.setPrecio(-10);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.update(1L, producto)
        );

        assertEquals("El precio no puede ser negativo", exception.getMessage());

        verify(productoRepository, never()).save(any());
    }


    @Test
    void create_DeberiaLanzarValidationExceptionSiLaCantidadEsNegativa() {
        Producto producto = new Producto();
        producto.setArticulo("Producto de prueba");
        producto.setPrecio(100);
        producto.setStock(-5);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.create(producto);
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
    }

    @Test
    void delete_DeberiaLanzarValidationExceptionSiElIdNoExiste() {
        Long idInexistente = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productoService.delete(idInexistente);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    @Test
    void create_DeberiaCrearProductoCorrectamente() {
        
        when(productoRepository.save(any()))
            .thenAnswer(invocation -> {
                Producto p = invocation.getArgument(0);
                p.setId(1L);
                return p;
        });
        
        Producto productoGuardado = productoService.create(productoExistente);

        assertNotNull(productoGuardado.getId());
        assertEquals("prueba 1", productoGuardado.getArticulo());
        verify(productoRepository).save(productoExistente);
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiElProductoYaExiste() {
        Producto productoNuevo = new Producto();
        productoNuevo.setArticulo("prueba 1");
        productoNuevo.setPrecio(1500);
        productoNuevo.setStock(10);

        when(productoRepository.existsByArticulo("prueba 1"))
            .thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            productoService.create(productoNuevo);
        });

        assertEquals("El artículo ya existe", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void update_DeberiaActualizarProductoCorrectamente() {

        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        when(productoRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Producto actualizado = new Producto();
        actualizado.setArticulo("Producto de prueba");
        actualizado.setPrecio(100);
        actualizado.setStock(10);

        Producto result = productoService.update(1L, actualizado);

        assertEquals("Producto de prueba", result.getArticulo());
        assertEquals(100, result.getPrecio());
        assertEquals(10, result.getStock());
    }

    @Test
    void delete_DeberiaEliminarProductoCorrectamente() {

        when(productoRepository.existsById(1L))
            .thenReturn(true);

        productoService.delete(1L);

        verify(productoRepository).deleteById(1L);
    }


    @Test
    void modifyStock_DeberiaModificarStockCorrectamente() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        productoService.modifyStock(1L, 5);
        assertEquals(20, productoExistente.getStock());
    }

    @Test
    void modifyStock_DeberiaLanzarValidationExceptionSiLaCantidadEsNegativa() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        StockInsufficientException exception = assertThrows(
            StockInsufficientException.class,
            () -> productoService.modifyStock(1L, -50)
        );

        assertEquals("Stock insuficiente para la operación", exception.getMessage());

        verify(productoRepository, never()).save(any());
    }

    @Test
    void update_DeberiaLanzarResourceNotFoundExceptionSiElIdNoExiste() {

        when(productoRepository.findById(2L))
            .thenReturn(Optional.empty());

        Producto actualizado = new Producto();
        actualizado.setArticulo("Producto de prueba");
        actualizado.setPrecio(100);
        actualizado.setStock(10);

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.update(2L, actualizado)
        );

        assertEquals("Recurso no encontrado", exception.getMessage());

        verify(productoRepository, never()).save(any());
    }


    @Test
    void findById_DeberiaLanzarValidationExceptionSiElIdNoExiste() {
        Long idInexistente = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productoService.findById(idInexistente);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    @Test
    void findById_DeberiaRetornarProductoCorrectamente() {
        when (productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));
        
        assertEquals(productoExistente, productoService.findById(1L));
    }

    @Test
    void findAll_DeberiaRetornarListaDeProductos() {

        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findAll())
            .thenReturn(expectedProducts);
        assertEquals(expectedProducts, productoService.findAll());
        
    }

    @Test
    void create_DeberiaCrearMultiplesProductosCorrectamente() {
        when(productoRepository.save(any()))
            .thenAnswer(invocation -> {
                Producto p = invocation.getArgument(0);
                if (p.getId() == null) {
                    p.setId(System.currentTimeMillis());
                }
                return p;
            });
        
        Producto producto1 = productoService.create(productoExistente);
        Producto producto2 = productoService.create(productoExistente2);

        assertNotNull(producto1.getId());
        assertNotNull(producto2.getId());
        assertEquals("prueba 1", producto1.getArticulo());
        assertEquals("prueba 2", producto2.getArticulo());
        assertNotEquals(producto1.getId(), producto2.getId());
    }

    @Test
    void findAll_DeberiaRetornarDosProductos() {
        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findAll())
            .thenReturn(expectedProducts);
        
        List<Producto> productos = productoService.findAll();

        assertEquals(2, productos.size());
        assertTrue(productos.contains(productoExistente));
        assertTrue(productos.contains(productoExistente2));
        assertEquals(1200, productos.get(0).getPrecio());
        assertEquals(99, productos.get(1).getPrecio());
    }

    @Test
    void modifyStock_DeberiaDisminuirStockCorrectamente() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        productoService.modifyStock(1L, -5); 
        
        assertEquals(10, productoExistente.getStock());
        verify(productoRepository).save(productoExistente);
    }

    @Test
    void modifyStock_DeberiaAumentarStockCorrectamente() {
        when(productoRepository.findById(2L))
            .thenReturn(Optional.of(productoExistente2));

        productoService.modifyStock(2L, 25);
        
        assertEquals(75, productoExistente2.getStock());
        verify(productoRepository).save(productoExistente2);
    }

    @Test
    void update_DeberiaActualizarMultiplesCamposDeProducto() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        when(productoRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Producto actualizado = new Producto();
        actualizado.setArticulo("prueba 1");
        actualizado.setPrecio(1500);
        actualizado.setStock(8);
        actualizado.setDescripcion("description prueba 1");

        Producto result = productoService.update(1L, actualizado);

        assertEquals("prueba 1", result.getArticulo());
        assertEquals(1500, result.getPrecio());
        assertEquals(8, result.getStock());
        assertEquals("description prueba 1", result.getDescripcion());
    }

    @Test
    void delete_DeberiaEliminarProductosPorId() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.existsById(2L)).thenReturn(true);

        productoService.delete(1L);
        productoService.delete(2L);

        verify(productoRepository).deleteById(1L);
        verify(productoRepository).deleteById(2L);
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiElArticuloEstaVacio() {
        Producto producto = new Producto();
        producto.setArticulo("");
        producto.setPrecio(100);
        producto.setStock(10);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.create(producto);
        });

        assertEquals("El artículo no puede estar vacío", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionSiStockEsNegativo() {
        when(productoRepository.findById(2L))
            .thenReturn(Optional.of(productoExistente2));

        Producto productoInvalido = new Producto();
        productoInvalido.setArticulo("prueba 1");
        productoInvalido.setPrecio(99);
        productoInvalido.setStock(-10);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.update(2L, productoInvalido);
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
    }

    @Test
    void findById_DeberiaRetornarProductoPorId() {
        when(productoRepository.findById(2L))
            .thenReturn(Optional.of(productoExistente2));

        Producto producto = productoService.findById(2L);

        assertNotNull(producto);
        assertEquals(2L, producto.getId());
        assertEquals("prueba 2", producto.getArticulo());
        assertEquals(99, producto.getPrecio());
        assertEquals(50, producto.getStock());
    }

    @Test
    void modifyStock_DeberiaLanzarExceptionSiStockSeVolveraNegativo() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        StockInsufficientException exception = assertThrows(
            StockInsufficientException.class,
            () -> productoService.modifyStock(1L, -20)
        );

        assertEquals("Stock insuficiente para la operación", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void create_DeberiaValidarTodosLosCamposRequeridos() {
        Producto productoIncompleto = new Producto();
        productoIncompleto.setArticulo(null);
        productoIncompleto.setPrecio(0);
        productoIncompleto.setStock(0);

        assertThrows(ValidationException.class, () -> {
            productoService.create(productoIncompleto);
        });
    }

    @Test
    void update_DeberiaValidarPrecioPositivo() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        Producto productoConPrecioInvalido = new Producto();
        productoConPrecioInvalido.setArticulo("prueba 1");
        productoConPrecioInvalido.setPrecio(-100);
        productoConPrecioInvalido.setStock(10);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.update(1L, productoConPrecioInvalido);
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaWhenNoExistenProductos() {
        when(productoRepository.findAll())
            .thenReturn(List.of());

        List<Producto> productos = productoService.findAll();

        assertTrue(productos.isEmpty());
        assertEquals(0, productos.size());
    }

    @Test
    void comparareProductosConDiferentesPrecios() {
        assertTrue(productoExistente.getPrecio() > productoExistente2.getPrecio());
        assertEquals(1200, productoExistente.getPrecio());
        assertEquals(99, productoExistente2.getPrecio());
    }

    @Test
    void comparareProductosConDiferentesStocks() {
        assertTrue(productoExistente2.getStock() > productoExistente.getStock());
        assertEquals(15, productoExistente.getStock());
        assertEquals(50, productoExistente2.getStock());
    }

    @Test
    void findById_DeberiaLanzarExceptionCuandoProductoNoExiste() {
        when(productoRepository.findById(3L))
            .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.findById(3L)
        );

        assertEquals("Recurso no encontrado", exception.getMessage());
    }

    @Test
    void findByPrecioRange_DeberiaRetornarProductosDentroRango() {
       
        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findByPrecioBetween(50, 1300))
            .thenReturn(expectedProducts);

        List<Producto> productos = productoService.findByPrecioRange(50, 1300);

        assertEquals(2, productos.size());
        assertTrue(productos.contains(productoExistente));
        assertTrue(productos.contains(productoExistente2));
    }

    @Test
    void findByPrecioRange_DeberiaLanzarExceptionSiPrecioNegativo() {

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.findByPrecioRange(-1, 100)
        );

        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    void findByPrecioRange_DeberiaLanzarExceptionSiMinMayorQueMax() {

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.findByPrecioRange(200, 100)
        );

        assertEquals(
            "El rango de precios es inválido (min > max)",
            exception.getMessage()
        );
    }

    @Test
    void findByPrecioRange_DeberiaRetornarListaVaciaSiNoHayProductosEnRango() {

        when(productoRepository.findByPrecioBetween(10, 50))
            .thenReturn(List.of());

        List<Producto> productos =
            productoService.findByPrecioRange(10, 50);

        assertTrue(productos.isEmpty());
    }

    @Test
    void findByStock_DeberiaRetornarProductosConAlMenosStockEspecificado() {
       
        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findByStock(10))
            .thenReturn(expectedProducts);

        List<Producto> productos = productoService.findByStock(10);

        assertEquals(2, productos.size());
        assertTrue(productos.contains(productoExistente));
        assertTrue(productos.contains(productoExistente2));
    }


    @Test
    void countTotal_DeberiaRetornarNumeroTotalDeProductos() {
        when(productoRepository.count())
            .thenReturn(5L);

        long total = productoService.countTotal();

        assertEquals(5L, total);
    }

    @Test
    void countTotal_DeberiaRetornarCeroCuandoNoHayProductos() {
        when(productoRepository.count())
            .thenReturn(0L);

        long total = productoService.countTotal();

        assertEquals(0L, total);
    }

    @Test
    void getTotalStock_DeberiaRetornarSumaTotalDeStocks() {
        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.getTotalStock())
            .thenReturn(65);

        int totalStock = productoService.getTotalStock();

        assertEquals(65, totalStock);
    }

    @Test
    void getTotalStock_DeberiaRetornarCeroCuandoNoHayProductos() {
        when(productoRepository.getTotalStock())
            .thenReturn(0);

        int totalStock = productoService.getTotalStock();

        assertEquals(0, totalStock);
    }

    @Test
    void getTotalStock_DeberiaRetornarCeroCuandoProductosTienenCeroStock() {
        Producto productoConCeroStock1 = new Producto();
        productoConCeroStock1.setId(3L);
        productoConCeroStock1.setArticulo("prueba 3");
        productoConCeroStock1.setPrecio(500);
        productoConCeroStock1.setStock(0);

        Producto productoConCeroStock2 = new Producto();
        productoConCeroStock2.setId(4L);
        productoConCeroStock2.setArticulo("prueba 4");
        productoConCeroStock2.setPrecio(800);
        productoConCeroStock2.setStock(0);

        List<Producto> expectedProducts = List.of(productoConCeroStock1, productoConCeroStock2);

        when(productoRepository.getTotalStock())
            .thenReturn(0);

        int totalStock = productoService.getTotalStock();

        assertEquals(0, totalStock);
    }

    @Test
    void findByStock_DeberiaRetornarValidationExceptionSiStockNegativo() {

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.findByStock(-5)
        );

        assertEquals("El stock no puede ser negativo", exception.getMessage());
    }

    @Test
    void findByArticuloContaining_DeberiaRetornarProductosQueContienenArticulo() {
        List<Producto> expectedProducts = List.of(productoExistente);

        when(productoRepository.findByArticuloContainingIgnoreCase("prueba 1"))
            .thenReturn(List.of(productoExistente));

        List<Producto> productos = productoService.findByArticuloContaining("prueba 1");

        assertEquals(1, productos.size());
        assertTrue(productos.contains(productoExistente));
    }

    @Test
    void findOutOfStockProducts_DeberiaRetornarProductosAgotados() {
        Producto productoAgotado1 = new Producto();
        productoAgotado1.setId(3L);
        productoAgotado1.setArticulo("prueba 3");
        productoAgotado1.setPrecio(500);
        productoAgotado1.setStock(0);

        Producto productoAgotado2 = new Producto();
        productoAgotado2.setId(4L);
        productoAgotado2.setArticulo("prueba 4");
        productoAgotado2.setPrecio(800);
        productoAgotado2.setStock(0);

        List<Producto> expectedProducts = List.of(productoAgotado1, productoAgotado2);

        when(productoRepository.findByStock(0))
            .thenReturn(expectedProducts);

        List<Producto> productos = productoService.findOutOfStockProducts();

        assertEquals(2, productos.size());
        assertTrue(productos.contains(productoAgotado1));
        assertTrue(productos.contains(productoAgotado2));
    }

    @Test
    void findByArticuloContaining_DeberiaLanzarValidationExceptionSiArticuloVacio() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.findByArticuloContaining("")
        );

        assertEquals("El término de artículo no puede estar vacío", exception.getMessage());
    }

    @Test
    void findByCompaniaIdAndCategoriaId_DeberiaRetornarProductosFiltrados() {
        List<Producto> expectedProducts = List.of(productoExistente);

        when(productoRepository.findByCompaniaIdAndCategoriaId(1L, 1L))
            .thenReturn(expectedProducts);

        List<Producto> productos = productoService.findByCompaniaIdAndCategoriaId(1L, 1L);

        assertEquals(1, productos.size());
        assertTrue(productos.contains(productoExistente));
    }

    @Test
    void findByCategoriaId_DeberiaLanzarValidationExceptionSiCategoriaIdNulo() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.findByCategoriaId(null)
        );

        assertEquals("El ID de la categoría no puede ser nulo", exception.getMessage());
    }

    @Test
    void findByCategoriaId_DeberiaRetornarProductosPorCategoriaId() {
        List<Producto> expectedProducts = List.of(productoExistente2);

        when(productoRepository.findByCategoriaId(2L))
            .thenReturn(expectedProducts);

        List<Producto> productos = productoService.findByCategoriaId(2L);

        assertEquals(1, productos.size());
        assertTrue(productos.contains(productoExistente2));
    }

    @Test
    void findByCompaniaId_DeberiaLanzarValidationExceptionSiCompaniaIdNulo() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.findByCompaniaId(null)
        );

        assertEquals("El ID de la compañía no puede ser nulo", exception.getMessage());
    }

    @Test
    void findByCompaniaId_DeberiaRetornarProductosPorCompaniaId() {
        List<Producto> expectedProducts = List.of(productoExistente);

        when(productoRepository.findByCompaniaId(1L))
            .thenReturn(expectedProducts);

        List<Producto> productos = productoService.findByCompaniaId(1L);

        assertEquals(1, productos.size());
        assertTrue(productos.contains(productoExistente));
    }

    @Test
    void existById_DeberiaRetornarTrueSiProductoExiste() {
        when(productoRepository.existsById(1L))
            .thenReturn(true);

        assertTrue(productoService.existById(1L));
    }
}

