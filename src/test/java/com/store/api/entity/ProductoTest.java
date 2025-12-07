package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Suite de tests unitarios para la entidad Producto.
 * 
 * Valida la construcción, getters, setters y comportamiento general de la clase Producto,
 * incluyendo inicialización con y sin parámetros, y manipulación de todos sus atributos.
 * 
 */
public class ProductoTest {

    /**
     * Verifica que se pueda crear una instancia de Producto con el constructor vacío.
     */
    @Test
    public void testConstructorVacio() {
        Producto producto = new Producto();

        assertNotNull(producto, "La instancia no debe ser nula");
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

        Producto producto = new Producto(id, articulo, descripcion, precio, categoria, stock, imagenUrl, compania);

        assertEquals(id, producto.getId(), "El ID debe coincidir");
        assertEquals(articulo, producto.getArticulo(), "El artículo debe coincidir");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe coincidir");
        assertEquals(precio, producto.getPrecio(), "El precio debe coincidir");
        assertEquals(categoria, producto.getCategoria(), "La categoría debe coincidir");
        assertEquals(stock, producto.getStock(), "El stock debe coincidir");
        assertEquals(imagenUrl, producto.getImagenUrl(), "La URL de imagen debe coincidir");
        assertEquals(compania, producto.getCompania(), "La compañía debe coincidir");
    }

    /**
     * Verifica que los setters y getters funcionan correctamente para todos los atributos.
     */
    @Test
    public void testSettersYGetters() {
        Producto producto = new Producto();
        String articulo = "123";
        String descripcion = "Bolso de cuero";
        int precio = 25;

        producto.setArticulo(articulo);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);

        assertEquals(articulo, producto.getArticulo(), "El artículo debe actualizarse");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe actualizarse");
        assertEquals(precio, producto.getPrecio(), "El precio debe actualizarse");
    }
}
