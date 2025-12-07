package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test unitario para la entidad Producto.
 * 
 * Verifica la construcción, getters y setters de la clase Producto.
 */
public class ProductoTest {

    @Test
    public void testConstructorVacio() {
        // Arrange & Act
        Producto producto = new Producto();

        // Assert
        assertNotNull(producto, "La instancia no debe ser nula");
    }

    @Test
    public void testConstructorConParametros() {
        // Arrange
        Long id = 1L;
        String articulo = "Laptop";
        String descripcion = "Laptop de alta gama";
        int precio = 1500;
        Categoria categoria = new Categoria(1L, "Electrónica", "Productos electrónicos");
        int stock = 10;
        String imagenUrl = "http://ejemplo.com/imagen.jpg";
        Compania compania = new Compania(1L, "CompañiaA");

        // Act
        Producto producto = new Producto(id, articulo, descripcion, precio, categoria, stock, imagenUrl, compania);

        // Assert
        assertEquals(id, producto.getId(), "El ID debe coincidir");
        assertEquals(articulo, producto.getArticulo(), "El artículo debe coincidir");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe coincidir");
        assertEquals(precio, producto.getPrecio(), "El precio debe coincidir");
        assertEquals(categoria, producto.getCategoria(), "La categoría debe coincidir");
        assertEquals(stock, producto.getStock(), "El stock debe coincidir");
        assertEquals(imagenUrl, producto.getImagenUrl(), "La URL de imagen debe coincidir");
        assertEquals(compania, producto.getCompania(), "La compañía debe coincidir");
    }

    @Test
    public void testSettersYGetters() {
        // Arrange
        Producto producto = new Producto();
        String articulo = "123";
        String descripcion = "Bolso de cuero";
        int precio = 25;

        // Act
        producto.setArticulo(articulo);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);

        // Assert
        assertEquals(articulo, producto.getArticulo(), "El artículo debe actualizarse");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe actualizarse");
        assertEquals(precio, producto.getPrecio(), "El precio debe actualizarse");
    }
}
