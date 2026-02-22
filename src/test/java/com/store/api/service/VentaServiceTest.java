package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.store.api.entity.Producto;
import com.store.api.entity.Venta;
import com.store.api.dto.venta.VentaCreateDTO;
import com.store.api.dto.venta.VentaUpdateDTO;
import com.store.api.dto.venta.VentaResponseDTO;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.VentaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test unitario para VentaService.
 * 
 * Verifica el comportamiento de la lógica de negocio de ventas.
 */
@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaService ventaService;

    private Venta venta;
    private VentaCreateDTO ventaCreateDTO;
    private Producto producto;
    private VentaResponseDTO ventaResponseDTO;

    @BeforeEach
    public void setUp() {
        // Setup base de entidad Venta para mocks del repositorio
        venta = new Venta();
        venta.setId(1L);
        venta.setCantidad(2);
        producto = new Producto();
        producto.setId(1L);
        producto.setArticulo("ProductoTest");
        producto.setStock(5);
        producto.setPrecioUnitario(50);
        producto.setPrecio(100);
        venta.setProducto(producto);

        // Setup para VentaCreateDTO
        ventaCreateDTO = new VentaCreateDTO();
        ventaCreateDTO.setCantidad(2);
        ventaCreateDTO.setProducto(producto);

        ventaResponseDTO = new VentaResponseDTO(
            venta.getId(),
            venta.getProducto(),
            venta.getCantidad(),
            venta.getFecha()
        );
    }
    
    // Test de función create
    @Test
    void create_DeberiaCrearVentaCorrectamente() {
        when(ventaRepository.save(any()))
        .thenAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        VentaResponseDTO ventaCreada = ventaService.create(ventaCreateDTO);
        assertNotNull(ventaCreada);
        assertEquals(2, ventaCreada.getCantidad());
        assertEquals(1L, ventaCreada.getId());
        assertEquals(100, ventaCreada.getPrecio());
        assertEquals(3, ventaCreada.getProducto().getStock());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoProductoEsNulo() {
        VentaCreateDTO ventaInvalida = new VentaCreateDTO();
        ventaInvalida.setProducto(null);
        ventaInvalida.setCantidad(1);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ventaService.create(ventaInvalida);
        });

        assertEquals("El producto de la venta no puede ser nulo", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoCantidadEsInvalida() {
        VentaCreateDTO ventaInvalida = new VentaCreateDTO();
        ventaInvalida.setProducto(new Producto());
        ventaInvalida.setCantidad(0);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ventaService.create(ventaInvalida);
        });

        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarDuplicateResourceExceptionCuandoIdYaExiste() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setArticulo("ProductoTest");
        producto.setStock(5);
        
        VentaCreateDTO ventaDuplicada = new VentaCreateDTO();
        ventaDuplicada.setId(1L);
        ventaDuplicada.setProducto(producto);
        ventaDuplicada.setCantidad(1);

        when(ventaRepository.existsById(1L)).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            ventaService.create(ventaDuplicada);
        });

        assertEquals("Ya existe una venta con el mismo ID", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoStockEsInsuficiente() {
        VentaCreateDTO ventaInvalida = new VentaCreateDTO();
        Producto productoConPocoStock = new Producto();
        productoConPocoStock.setStock(1);
        ventaInvalida.setProducto(productoConPocoStock);
        ventaInvalida.setCantidad(2);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ventaService.create(ventaInvalida);
        });

        assertEquals("No hay suficiente stock para completar la venta", exception.getMessage());
    }

    @Test
    void create_DeberiaRestarStockDelProductoDespuesDeCrearVenta() {
        when(ventaRepository.save(any()))
        .thenAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        int stockInicial = ventaCreateDTO.getProducto().getStock();
        ventaService.create(ventaCreateDTO);
        int stockFinal = ventaCreateDTO.getProducto().getStock();

        assertEquals(stockInicial - ventaCreateDTO.getCantidad(), stockFinal);
    }

    // Test de función findAll
    @Test
    void findAll_DeberiaRetornarListaDeVentasEnFormatoDTO() {
        when(ventaRepository.findAll())
            .thenReturn(List.of(venta));

        List<VentaResponseDTO> ventas = ventaService.findAll();

        assertNotNull(ventas);
        assertEquals(1, ventas.size());
        assertEquals(venta.getId(), ventas.get(0).getId());
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaWhenNoHayVentas() {
        when(ventaRepository.findAll())
            .thenReturn(List.of());

        List<VentaResponseDTO> ventas = ventaService.findAll();

        assertNotNull(ventas);
        assertTrue(ventas.isEmpty());
    }

    // Test de función findById
    @Test
    void findById_DeberiaRetornarVentaPorId() {
        when(ventaRepository.findById(1L))
            .thenReturn(Optional.of(venta));

        VentaResponseDTO ventaFound = ventaService.findById(1L);

        assertNotNull(ventaFound);
        assertEquals(venta.getId(), ventaFound.getId());
        assertEquals(venta.getCantidad(), ventaFound.getCantidad());
    }

    @Test
    void findById_DeberiaLanzarResourceNotFoundExceptionWhenVentaNoExiste() {
        when(ventaRepository.findById(999L))
            .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ventaService.findById(999L);
        });

        assertNotNull(exception);
    }

    // Test de función update
    @Test
    void update_DeberiaActualizarVentaCorrectamente() {
        Venta existingVenta = new Venta();
        existingVenta.setId(1L);
        existingVenta.setCantidad(2);
        existingVenta.setProducto(producto);

        VentaUpdateDTO updateDTO = new VentaUpdateDTO();
        updateDTO.setCantidad(3);
        updateDTO.setProducto(producto);

        when(ventaRepository.findById(1L))
            .thenReturn(Optional.of(existingVenta));
        when(ventaRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        VentaResponseDTO updated = ventaService.update(1L, updateDTO);

        assertNotNull(updated);
        assertEquals(3, updated.getCantidad());
    }

    @Test
    void update_DeberiaLanzarResourceNotFoundExceptionWhenVentaNoExiste() {
        VentaUpdateDTO updateDTO = new VentaUpdateDTO();
        updateDTO.setCantidad(3);
        updateDTO.setProducto(producto);

        when(ventaRepository.findById(999L))
            .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ventaService.update(999L, updateDTO);
        });

        assertNotNull(exception);
    }

    // Test de función delete
    @Test
    void delete_DeberiaEliminarVentaCorrectamente() {
        when(ventaRepository.existsById(1L))
            .thenReturn(true);

        assertDoesNotThrow(() -> ventaService.delete(1L));
    }

    @Test
    void delete_DeberiaLanzarResourceNotFoundExceptionWhenVentaNoExiste() {
        when(ventaRepository.existsById(999L))
            .thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ventaService.delete(999L);
        });

        assertNotNull(exception);
    }

    // Test de funcion ventasDeHoy
    @Test
    void ventasDeHoy_DeberiaRetornarDosVentasDelDiaConMismoProducto() {
        Producto productoHoy = new Producto();
        productoHoy.setId(2L);
        productoHoy.setArticulo("Test");
        productoHoy.setStock(5);

        Venta ventaHoyUno = new Venta();
        ventaHoyUno.setId(2L);
        ventaHoyUno.setCantidad(1);
        ventaHoyUno.setProducto(productoHoy);
        Venta ventaHoyDos = new Venta();
        ventaHoyDos.setId(3L);
        ventaHoyDos.setCantidad(3);
        ventaHoyDos.setProducto(productoHoy);

        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of(ventaHoyUno, ventaHoyDos));
        List<VentaResponseDTO> ventasHoy = ventaService.ventasDeHoy();

        assertNotNull(ventasHoy);
        assertEquals(2, ventasHoy.size());
        assertEquals(2L, ventasHoy.get(0).getId());
        assertEquals(1, ventasHoy.get(0).getCantidad());
        assertEquals(3L, ventasHoy.get(1).getId());
        assertEquals(3, ventasHoy.get(1).getCantidad());
    }

    @Test
    void ventasDeHoy_DeberiaRetornarListaVaciaCuandoNoHayVentasHoy() {
        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of());
        List<VentaResponseDTO> ventasHoy = ventaService.ventasDeHoy();

        assertNotNull(ventasHoy);
        assertTrue(ventasHoy.isEmpty());
    }

    // Test de funcion ventasDeLaSemana
    @Test
    void ventasDeLaSemana_DeberiaRetornarDosVentasDeLaSemanaConMismoProducto() {
        Producto productoSemana = new Producto();
        productoSemana.setId(3L);
        productoSemana.setArticulo("SemanaTest");
        productoSemana.setStock(5);

        Venta ventaSemanaUno = new Venta();
        ventaSemanaUno.setId(4L);
        ventaSemanaUno.setCantidad(2);
        ventaSemanaUno.setProducto(productoSemana);
        Venta ventaSemanaDos = new Venta();
        ventaSemanaDos.setId(5L);
        ventaSemanaDos.setCantidad(1);
        ventaSemanaDos.setProducto(productoSemana);

        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of(ventaSemanaUno, ventaSemanaDos));
        List<VentaResponseDTO> ventasSemana = ventaService.ventasDeLaSemana();

        assertNotNull(ventasSemana);
        assertEquals(2, ventasSemana.size());
        assertEquals(4L, ventasSemana.get(0).getId());
        assertEquals(2, ventasSemana.get(0).getCantidad());
        assertEquals(5L, ventasSemana.get(1).getId());
        assertEquals(1, ventasSemana.get(1).getCantidad());
    }

    @Test
    void ventasDeLaSemana_DeberiaRetornarListaVaciaCuandoNoHayVentasEnLaSemana() {
        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of());
        List<VentaResponseDTO> ventasSemana = ventaService.ventasDeLaSemana();

        assertNotNull(ventasSemana);
        assertTrue(ventasSemana.isEmpty());
    }
    // Test de funcion ventasDelMes
    @Test
    void ventasDelMes_DeberiaRetornarDosVentasDelMes() {
        Producto productoMes = new Producto();
        productoMes.setId(4L);
        productoMes.setArticulo("MesTest");
        productoMes.setStock(5);

        Venta ventaMesUno = new Venta();
        ventaMesUno.setId(6L);
        ventaMesUno.setCantidad(4);
        ventaMesUno.setProducto(productoMes);
        Venta ventaMesDos = new Venta();
        ventaMesDos.setId(7L);
        ventaMesDos.setCantidad(2);
        ventaMesDos.setProducto(productoMes);

        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of(ventaMesUno, ventaMesDos));
        List<VentaResponseDTO> ventasMes = ventaService.ventasDelMes();

        assertNotNull(ventasMes);
        assertEquals(2, ventasMes.size());
        assertEquals(6L, ventasMes.get(0).getId());
        assertEquals(4, ventasMes.get(0).getCantidad());
        assertEquals(7L, ventasMes.get(1).getId());
        assertEquals(2, ventasMes.get(1).getCantidad());
    }

    @Test
    void ventasDelMes_DeberiaRetornarListaVaciaCuandoNoHayVentasEnElMes() {
        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of());
        List<VentaResponseDTO> ventasMes = ventaService.ventasDelMes();

        assertNotNull(ventasMes);
        assertTrue(ventasMes.isEmpty());
    }

    // Test de funcion ventasEntre
    @Test
    void ventasEntre_DeberiaRetornarVentasEnRangoDeFechas() {

        Producto productoRango = new Producto();
        productoRango.setId(5L);
        productoRango.setArticulo("RangoTest");
        productoRango.setStock(5);

        Venta ventaRangoUno = new Venta();
        ventaRangoUno.setId(8L);
        ventaRangoUno.setCantidad(1);
        ventaRangoUno.setProducto(productoRango);
        Venta ventaRangoDos = new Venta();
        ventaRangoDos.setId(9L);
        ventaRangoDos.setCantidad(3);
        ventaRangoDos.setProducto(productoRango);

        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of(ventaRangoUno, ventaRangoDos));
        List<VentaResponseDTO> ventasRango = ventaService.ventasEntre(
            null,
            null
        );

        assertNotNull(ventasRango);
        assertEquals(2, ventasRango.size());
        assertEquals(8L, ventasRango.get(0).getId());
        assertEquals(1, ventasRango.get(0).getCantidad());
        assertEquals(9L, ventasRango.get(1).getId());
        assertEquals(3, ventasRango.get(1).getCantidad());
    }

    @Test
    void update_NoDebeValidarDuplicadoPorId_EnFlujoDeActualizacion() {
        Venta existingVenta = new Venta();
        existingVenta.setId(1L);
        existingVenta.setCantidad(2);
        existingVenta.setProducto(producto);

        VentaUpdateDTO updateDTO = new VentaUpdateDTO();
        updateDTO.setCantidad(1);
        updateDTO.setProducto(producto);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(existingVenta));
        when(ventaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> ventaService.update(1L, updateDTO));
        verify(ventaRepository, never()).existsById(1L);
    }

    // Test de funcion getPrecioUnitario
    @Test
    void getPrecioUnitario_DeberiaRetornarPrecioUnitarioCorrecto() {
        Integer precioUnitario = ventaService.getPrecioUnitario(ventaResponseDTO);
        assertEquals(50, precioUnitario);
    }

    // Test de funcion getPrecio
    @Test
    void getPrecio_DeberiaRetornarPrecioCorrecto() {
        Integer precio = ventaService.getPrecio(ventaResponseDTO);
        assertEquals(100, precio);
    }

    // Test de funcion getTotalPrecioUnitario
    @Test
    void getTotalPrecioUnitario_DeberiaRetornarTotalPrecioUnitarioCorrecto() {
        Integer totalPrecioUnitario = ventaService.getTotalPrecioUnitario(ventaResponseDTO);
        assertEquals(100, totalPrecioUnitario);
    }

    // Test de funcion getTotalPrecioCliente
    @Test
    void getTotalPrecioCliente_DeberiaRetornarTotalPrecioClienteCorrecto() {
        Integer totalPrecioCliente = ventaService.getTotalPrecioCliente(ventaResponseDTO);
        assertEquals(200, totalPrecioCliente);
    }
}
