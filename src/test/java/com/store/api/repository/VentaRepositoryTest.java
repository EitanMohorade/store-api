package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.store.api.entity.Producto;
import com.store.api.entity.Venta;

/**
 * Suite de tests para VentaRepository.
 * 
 * Valida las operaciones CRUD (Create, Read, Update, Delete) y comportamientos
 * específicos de la entidad Venta en la base de datos H2 de prueba.
 * 
 * Los tests cubren:
 * - Creación de ventas con productos asociados
 * - Recuperación de ventas por ID
 * - Actualización de cantidades en ventas
 * - Eliminación de ventas por ID
 * - Búsqueda y conteo de múltiples ventas
 * - Validación de restricciones (e.g., venta sin producto)
 * - Cálculo de precios unitarios y totales
 * 
 */
@DataJpaTest
public class VentaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VentaRepository ventaRepository;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setArticulo("Lapicera");
        producto.setPrecio(200);
        producto.setPrecioUnitario(150);
        producto.setStock(100);
        entityManager.persist(producto);
    }

    @Test
    void debeCrearYEncontrarVenta() {
        Venta venta = new Venta(null, producto, 3);
        entityManager.persist(venta);

        Venta encontrada = ventaRepository.findById(venta.getId()).orElse(null);

        assertNotNull(encontrada);
        assertEquals(3, encontrada.getCantidad());
        assertEquals(producto.getId(), encontrada.getProducto().getId());
    }

    @Test
    void debeEliminarVentaDelRepositorio() {
        Venta venta = new Venta(null, producto, 2);
        entityManager.persist(venta);

        ventaRepository.deleteById(venta.getId());

        assertFalse(ventaRepository.findById(venta.getId()).isPresent());
    }

    @Test
    void debeActualizarVentaEnElRepositorio() {
        Venta venta = new Venta(null, producto, 1);
        entityManager.persist(venta);

        venta.setCantidad(10);
        ventaRepository.save(venta);

        Venta actualizada = ventaRepository.findById(venta.getId()).get();
        assertEquals(10, actualizada.getCantidad());
    }

    @Test
    void debeContarVentasEnElRepositorio() {
        entityManager.persist(new Venta(null, producto, 1));
        entityManager.persist(new Venta(null, producto, 4));

        long count = ventaRepository.count();

        assertEquals(2, count);
    }

    @Test
    void debeEncontrarTodasLasVentasEnElRepositorio() {
        entityManager.persist(new Venta(null, producto, 3));
        entityManager.persist(new Venta(null, producto, 5));

        var ventas = ventaRepository.findAll();

        assertEquals(2, ventas.size());
    }

    @Test
    void debeLanzarExcepcionAlCrearVentaSinProducto() {
        Venta venta = new Venta(null, null, 3);

        assertThrows(Exception.class, () -> {
            ventaRepository.saveAndFlush(venta);
        });
    }

    @Test
    void debeCalcularPrecioUnitarioTransient() {
        Venta v = new Venta(null, producto, 2);

        assertEquals(150, v.getPrecioUnitario());
        assertEquals(300, v.getTotalPrecioUnitario());
    }

    @Test
    void debeCalcularPrecioClienteTransient() {
        Venta v = new Venta(null, producto, 2);

        assertEquals(200, v.getPrecio());
        assertEquals(400, v.getTotalPrecioCliente());
    }

    @Test
    void getPrecioUnitario_debeRetornarPrecioUnitarioDelProducto() {
        Producto producto = new Producto();
        producto.setPrecioUnitario(150);

        Venta venta = new Venta();
        venta.setProducto(producto);

        assertEquals(150, venta.getPrecioUnitario());
    }

    @Test
    void getPrecioUnitario_conProductoNullDebeRetornarCero() {
        Venta venta = new Venta();
        venta.setProducto(null);

        assertEquals(0, venta.getPrecioUnitario());
    }

    @Test
    void getPrecio_debeRetornarPrecioDelProducto() {
        Producto producto = new Producto();
        producto.setPrecio(300);

        Venta venta = new Venta();
        venta.setProducto(producto);

        assertEquals(300, venta.getPrecio());
    }

    @Test
    void getPrecio_conProductoNullDebeRetornarCero() {
        Venta venta = new Venta();
        venta.setProducto(null);

        assertEquals(0, venta.getPrecio());
    }

    @Test
    void getTotalPrecioUnitario_debeCalcularCorrectamente() {
        Producto producto = new Producto();
        producto.setPrecioUnitario(100);

        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setCantidad(5);

        assertEquals(500, venta.getTotalPrecioUnitario());
    }

    @Test
    void getTotalPrecioUnitario_conCantidadNullDebeSerCero() {
        Producto producto = new Producto();
        producto.setPrecioUnitario(100);

        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setCantidad(null);

        assertEquals(0, venta.getTotalPrecioUnitario());
    }

    @Test
    void getTotalPrecioUnitario_conProductoNullDebeSerCero() {
        Venta venta = new Venta();
        venta.setProducto(null);
        venta.setCantidad(5);

        assertEquals(0, venta.getTotalPrecioUnitario());
    }

    @Test
    void getTotalPrecioCliente_debeCalcularCorrectamente() {
        Producto producto = new Producto();
        producto.setPrecio(200);

        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setCantidad(3);

        assertEquals(600, venta.getTotalPrecioCliente());
    }

    @Test
    void getTotalPrecioCliente_conCantidadNullDebeSerCero() {
        Producto producto = new Producto();
        producto.setPrecio(200);

        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setCantidad(null);

        assertEquals(0, venta.getTotalPrecioCliente());
    }

    @Test
    void getTotalPrecioCliente_conProductoNullDebeSerCero() {
        Venta venta = new Venta();
        venta.setProducto(null);
        venta.setCantidad(4);

        assertEquals(0, venta.getTotalPrecioCliente());
    }

}
