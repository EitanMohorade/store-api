package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


import com.store.api.entity.Producto;
import com.store.api.dto.producto.ProductoCreateDTO;
import com.store.api.dto.producto.ProductoUpdateDTO;
import com.store.api.dto.producto.ProductoResponseDTO;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.StockInsufficientException;
import com.store.api.exception.ValidationException;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private ProductoCreateDTO createDTO;
    private ProductoCreateDTO createDTO2;
    private ProductoUpdateDTO updateDTO;

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

        createDTO = new ProductoCreateDTO();
        createDTO.setArticulo("prueba 1");
        createDTO.setPrecio(1200);
        createDTO.setStock(15);
        createDTO.setDescripcion("description prueba 1");
        createDTO.setCategoria(null);

        createDTO2 = new ProductoCreateDTO();
        createDTO2.setArticulo("prueba 2");
        createDTO2.setPrecio(99);
        createDTO2.setStock(50);
        createDTO2.setDescripcion("description prueba 2");
        createDTO2.setCategoria(null);

        updateDTO = new ProductoUpdateDTO();
        updateDTO.setArticulo("Producto de prueba");
        updateDTO.setPrecio(100);
        updateDTO.setStock(10);
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiElArticuloEsNulo() {
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setArticulo(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.create(dto);
        });

        assertEquals("El artículo no puede estar vacío",exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionSiElPrecioEsNegativo() {

        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setArticulo("Producto de prueba");
        dto.setPrecio(-10);
        dto.setStock(10);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> productoService.update(1L, dto)
        );

        assertEquals("El precio no puede ser negativo", exception.getMessage());

        verify(productoRepository, never()).save(any());
    }


    @Test
    void create_DeberiaLanzarValidationExceptionSiLaCantidadEsNegativa() {
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setArticulo("Producto de prueba");
        dto.setPrecio(100);
        dto.setStock(-5);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.create(dto);
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
        
        ProductoResponseDTO productoGuardado = productoService.create(createDTO);

        assertNotNull(productoGuardado.getId());
        assertEquals("prueba 1", productoGuardado.getArticulo());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void create_DeberiaLanzarValidationExceptionSiElProductoYaExiste() {
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setArticulo("prueba 1");
        dto.setPrecio(1500);
        dto.setStock(10);

        when(productoRepository.existsByArticulo("prueba 1"))
            .thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            productoService.create(dto);
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

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setArticulo("Producto de prueba");
        dto.setPrecio(100);
        dto.setStock(10);

        ProductoResponseDTO result = productoService.update(1L, dto);

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

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setArticulo("Producto de prueba");
        dto.setPrecio(100);
        dto.setStock(10);

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.update(2L, dto)
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
        
        ProductoResponseDTO result = productoService.findById(1L);
        
        assertEquals(productoExistente.getId(), result.getId());
        assertEquals(productoExistente.getArticulo(), result.getArticulo());
        assertEquals(productoExistente.getPrecio(), result.getPrecio());
    }

    @Test
    void findAll_DeberiaRetornarListaDeProductos() {

        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findAll())
            .thenReturn(expectedProducts);
        
        List<ProductoResponseDTO> result = productoService.findAll();
        
        assertEquals(2, result.size());
        assertEquals("prueba 1", result.get(0).getArticulo());
        assertEquals("prueba 2", result.get(1).getArticulo());
    }

    @Test
    void findAll_DeberiaRetornarDosProductos() {
        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findAll())
            .thenReturn(expectedProducts);
        
        List<ProductoResponseDTO> productos = productoService.findAll();

        assertEquals(2, productos.size());
        assertEquals("prueba 1", productos.get(0).getArticulo());
        assertEquals("prueba 2", productos.get(1).getArticulo());
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

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setArticulo("prueba 1");
        dto.setPrecio(1500);
        dto.setStock(8);
        dto.setDescripcion("description prueba 1");

        ProductoResponseDTO result = productoService.update(1L, dto);

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
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setArticulo("");
        dto.setPrecio(100);
        dto.setStock(10);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.create(dto);
        });

        assertEquals("El artículo no puede estar vacío", exception.getMessage());
    }

    @Test
    void update_DeberiaLanzarValidationExceptionSiStockEsNegativo() {
        when(productoRepository.findById(2L))
            .thenReturn(Optional.of(productoExistente2));

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setArticulo("prueba 1");
        dto.setPrecio(99);
        dto.setStock(-10);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.update(2L, dto);
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
    }

    @Test
    void findById_DeberiaRetornarProductoPorId() {
        when(productoRepository.findById(2L))
            .thenReturn(Optional.of(productoExistente2));

        ProductoResponseDTO producto = productoService.findById(2L);

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
        ProductoCreateDTO dto = new ProductoCreateDTO();
        dto.setArticulo(null);
        dto.setPrecio(0);
        dto.setStock(0);

        assertThrows(ValidationException.class, () -> {
            productoService.create(dto);
        });
    }

    @Test
    void update_DeberiaValidarPrecioPositivo() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(productoExistente));

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setArticulo("prueba 1");
        dto.setPrecio(-100);
        dto.setStock(10);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productoService.update(1L, dto);
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaWhenNoExistenProductos() {
        when(productoRepository.findAll())
            .thenReturn(List.of());

        List<ProductoResponseDTO> productos = productoService.findAll();

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

        List<ProductoResponseDTO> productos = productoService.findByPrecioRange(50, 1300);

        assertEquals(2, productos.size());
        assertEquals("prueba 1", productos.get(0).getArticulo());
        assertEquals("prueba 2", productos.get(1).getArticulo());
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

        List<ProductoResponseDTO> productos =
            productoService.findByPrecioRange(10, 50);

        assertTrue(productos.isEmpty());
    }

    @Test
    void findByStock_DeberiaRetornarProductosConAlMenosStockEspecificado() {
       
        List<Producto> expectedProducts = List.of(productoExistente, productoExistente2);

        when(productoRepository.findByStock(10))
            .thenReturn(expectedProducts);

        List<ProductoResponseDTO> productos = productoService.findByStock(10);

        assertEquals(2, productos.size());
        assertEquals("prueba 1", productos.get(0).getArticulo());
        assertEquals("prueba 2", productos.get(1).getArticulo());
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

        List<ProductoResponseDTO> productos = productoService.findByArticuloContaining("prueba 1");

        assertEquals(1, productos.size());
        assertEquals("prueba 1", productos.get(0).getArticulo());
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

        List<ProductoResponseDTO> productos = productoService.findOutOfStockProducts();

        assertEquals(2, productos.size());
        assertEquals("prueba 3", productos.get(0).getArticulo());
        assertEquals("prueba 4", productos.get(1).getArticulo());
        assertEquals(0, productos.get(0).getStock());
        assertEquals(0, productos.get(1).getStock());
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

        List<ProductoResponseDTO> productos = productoService.findByCompaniaIdAndCategoriaId(1L, 1L);

        assertEquals(1, productos.size());
        assertEquals("prueba 1", productos.get(0).getArticulo());
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

        List<ProductoResponseDTO> productos = productoService.findByCategoriaId(2L);

        assertEquals(1, productos.size());
        assertEquals("prueba 2", productos.get(0).getArticulo());
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

        List<ProductoResponseDTO> productos = productoService.findByCompaniaId(1L);

        assertEquals(1, productos.size());
        assertEquals("prueba 1", productos.get(0).getArticulo());
    }

    @Test
    void existById_DeberiaRetornarTrueSiProductoExiste() {
        when(productoRepository.existsById(1L))
            .thenReturn(true);

        assertTrue(productoService.existById(1L));
    }
}

