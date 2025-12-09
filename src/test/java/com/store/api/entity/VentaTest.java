package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Suite de tests unitarios para la entidad Venta.
 * 
 * Valida la construcción, getters, setters y comportamiento general de la clase Venta,
 * incluyendo inicialización con y sin parámetros, y manipulación de atributos.
 * Además, funcionalidades específicas como el cálculo de totales y la inicialización automática de la fecha.
 * 
 */
public class VentaTest {
    
    /**
     * Verifica que se pueda crear una instancia de Producto con el constructor vacío.
     */
    @Test
    public void testConstructorVacio() {
        Venta venta = new Venta();

        assertNotNull(venta, "La instancia no debe ser nula");
    }

    /**
     * Verifica que el constructor con parámetros inicialice correctamente todos los atributos.
     */
    @Test
    public void testConstructorConParametros() {
        Long id = 1L;
        String articulo = "Cartera";
        String descripcion = "Cartera de alta gama";
        int precio = 1500;
        Categoria categoria = new Categoria(1L, "Mochila", "Productos de mochila");
        int stock = 10;
        String imagenUrl = "http://ejemplo.com/imagen.jpg";
        Compania compania = new Compania(1L, "GC");
        int precioUnitario = 150;

        Venta venta = new Venta(null, new Producto(id, articulo, descripcion, precio, categoria, stock, imagenUrl, compania, precioUnitario), 5);

        assertEquals(5, venta.getCantidad(), "La cantidad debe coincidir");
        assertEquals(articulo, venta.getProducto().getArticulo(), "El artículo del producto debe coincidir");
        assertEquals(descripcion, venta.getProducto().getDescripcion(), "La descripción del producto debe coincidir");
        assertEquals(precio, venta.getProducto().getPrecio(), "El precio del producto debe coincidir");
        assertEquals(categoria, venta.getProducto().getCategoria(), "La categoría del producto debe coincidir");
        assertEquals(stock, venta.getProducto().getStock(), "El stock del producto debe coincidir");
        assertEquals(imagenUrl, venta.getProducto().getImagenUrl(), "La URL de imagen del producto debe coincidir");
        assertEquals(compania, venta.getProducto().getCompania(), "La compañía del producto debe coincidir");
        assertEquals(precioUnitario, venta.getProducto().getPrecioUnitario(), "El precio unitario del producto debe coincidir");
    }

    /**
     * Verifica que los setters y getters funcionan correctamente para todos los atributos.
     */
    @Test
    public void testSettersYGetters() {

        Venta venta = new Venta();

        Long id = 1L;
        Producto producto = new Producto();
        int cantidad = 3;

        venta.setCantidad(cantidad);
        venta.setProducto(producto);
        venta.setId(id);

        assertEquals(producto, venta.getProducto(), "El producto debe actualizarse");
        assertEquals(cantidad, venta.getCantidad(), "La cantidad debe actualizarse");
        assertEquals(id, venta.getId(), "El ID debe actualizarse");

    }
    
    @Test
    public void testGetPrecioYGetPrecioUnitarioConProductoNoNulo() {
        Producto p = new Producto();
        p.setPrecio(500);
        p.setPrecioUnitario(100);

        Venta v = new Venta();
        v.setProducto(p);

        assertEquals(Integer.valueOf(100), v.getPrecioUnitario(), "El precio unitario debe coincidir");
        assertEquals(Integer.valueOf(500), v.getPrecio(), "El precio debe coincidir");
    }

    @Test
    public void testGetPrecioYGetPrecioUnitarioConProductoNulo() {
        Venta v = new Venta();
        v.setProducto(null);

        assertEquals(Integer.valueOf(0), v.getPrecioUnitario(), "Si no hay producto, precio unitario debe ser 0");
        assertEquals(Integer.valueOf(0), v.getPrecio(), "Si no hay producto, precio debe ser 0");
    }

    @Test
    public void testTotalesDeVenta() {
        Producto p = new Producto();
        p.setPrecioUnitario(100);
        p.setPrecio(250);

        Venta v = new Venta();
        v.setProducto(p);
        v.setCantidad(4);

        assertEquals(Integer.valueOf(400), v.getTotalPrecioUnitario(), "Total unitario debe ser cantidad * precioUnitario");
        assertEquals(Integer.valueOf(1000), v.getTotalPrecioCliente(), "Total cliente debe ser cantidad * precio");
    }

    @Test
    public void testTotalesConCantidadNula() {
        Producto p = new Producto();
        p.setPrecioUnitario(100);
        p.setPrecio(250);

        Venta v = new Venta();
        v.setProducto(p);
        v.setCantidad(null); 

        assertEquals(Integer.valueOf(0), v.getTotalPrecioUnitario(), "Con cantidad null, total unitario debe ser 0");
        assertEquals(Integer.valueOf(0), v.getTotalPrecioCliente(), "Con cantidad null, total cliente debe ser 0");
    }

    @Test
    public void testFechaSeInicializaAutomatica() {
        Venta v = new Venta();

        assertNotNull(v.getFecha(), "La fecha no debe ser nula");

        LocalDateTime ahora = LocalDateTime.now();
        assertTrue(
            v.getFecha().isBefore(ahora.plusSeconds(2)) &&
            v.getFecha().isAfter(ahora.minusSeconds(2)),
            "La fecha debe ser cercana a la actual"
        );
    }

    @Test
    public void testConstructorConParametrosGeneraFecha() {
        Producto p = new Producto();
        Venta v = new Venta(null, p, 3);

        assertNotNull(v.getFecha(), "La fecha debe generarse en el constructor");
    }
}
