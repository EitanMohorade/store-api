package com.store.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import com.store.api.entity.Producto;
import com.store.api.entity.Venta;
import com.store.api.exception.DuplicateResourceException;
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
    @BeforeEach
    public void setUp() {
        venta = new Venta();
        venta.setId(1L);
        venta.setCantidad(2);
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setArticulo("ProductoTest");
        producto.setStock(5);
        producto.setPrecioUnitario(50);
        producto.setPrecio(100);
        venta.setProducto(producto);
    }
    
    // Test de funcion create
    @Test
    void create_DeberiaCrearVentaCorrectamente() {
        when(ventaRepository.save(any()))
        .thenAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        Venta ventaCreada = ventaService.create(venta);
        assertNotNull(ventaCreada);
        assertEquals(2, ventaCreada.getCantidad());
        assertEquals(1L, ventaCreada.getId());
        assertEquals(100, ventaCreada.getProducto().getPrecio());
        assertEquals(3, ventaCreada.getProducto().getStock());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoProductoEsNulo() {
        Venta ventaInvalida = new Venta(); 
        ventaInvalida.setProducto(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ventaService.create(ventaInvalida);
        });

        assertEquals("El producto de la venta no puede ser nulo", exception.getMessage());
    }

    @Test
    void create_DeberiaLanzarValidationExceptionCuandoCantidadEsInvalida() {
        Venta ventaInvalida = new Venta();
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
        
        Venta ventaDuplicada = new Venta();
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
        Venta ventaInvalida = new Venta();
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

        int stockInicial = venta.getProducto().getStock();
        ventaService.create(venta);
        int stockFinal = venta.getProducto().getStock();

        assertEquals(stockInicial - venta.getCantidad(), stockFinal);
    }

    // Testd de funcion ventasDeHoy
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
        List<Venta> ventasHoy = ventaService.ventasDeHoy();

        assertNotNull(ventasHoy);
        assertEquals(2, ventasHoy.size());
    }

    @Test
    void ventasDeHoy_DeberiaRetornarListaVaciaCuandoNoHayVentasHoy() {
        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of());
        List<Venta> ventasHoy = ventaService.ventasDeHoy();

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
        List<Venta> ventasSemana = ventaService.ventasDeLaSemana();

        assertNotNull(ventasSemana);
        assertEquals(2, ventasSemana.size());
    }

    @Test
    void ventasDeLaSemana_DeberiaRetornarListaVaciaCuandoNoHayVentasEnLaSemana() {
        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of());
        List<Venta> ventasSemana = ventaService.ventasDeLaSemana();

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
        List<Venta> ventasMes = ventaService.ventasDelMes();

        assertNotNull(ventasMes);
        assertEquals(2, ventasMes.size());
    }

    @Test
    void ventasDelMes_DeberiaRetornarListaVaciaCuandoNoHayVentasEnElMes() {
        when(ventaRepository.findByFechaBetween(any(), any()))
            .thenReturn(List.of());
        List<Venta> ventasMes = ventaService.ventasDelMes();

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
        List<Venta> ventasRango = ventaService.ventasEntre(
            null,
            null
        );

        assertNotNull(ventasRango);
        assertEquals(2, ventasRango.size());
    }

    // Test de funcion getPrecioUnitario
    @Test
    void getPrecioUnitario_DeberiaRetornarPrecioUnitarioCorrecto() {
        Integer precioUnitario = ventaService.getPrecioUnitario(venta);
        assertEquals(50, precioUnitario);
    }

    // Test de funcion getPrecio
    @Test
    void getPrecio_DeberiaRetornarPrecioCorrecto() {
        Integer precio = ventaService.getPrecio(venta);
        assertEquals(100, precio);
    }

    // Test de funcion getTotalPrecioUnitario
    @Test
    void getTotalPrecioUnitario_DeberiaRetornarTotalPrecioUnitarioCorrecto() {
        Integer totalPrecioUnitario = ventaService.getTotalPrecioUnitario(venta);
        assertEquals(100, totalPrecioUnitario);
    }

    // Test de funcion getTotalPrecioCliente
    @Test
    void getTotalPrecioCliente_DeberiaRetornarTotalPrecioClienteCorrecto() {
        Integer totalPrecioCliente = ventaService.getTotalPrecioCliente(venta);
        assertEquals(200, totalPrecioCliente);
    }
}
