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
        int precioUnitario = 150;

        Producto producto = new Producto(id, articulo, descripcion, precio, categoria, stock, imagenUrl, compania, precioUnitario);

        assertEquals(id, producto.getId(), "El ID debe coincidir");
        assertEquals(articulo, producto.getArticulo(), "El artículo debe coincidir");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe coincidir");
        assertEquals(precio, producto.getPrecio(), "El precio debe coincidir");
        assertEquals(categoria, producto.getCategoria(), "La categoría debe coincidir");
        assertEquals(stock, producto.getStock(), "El stock debe coincidir");
        assertEquals(imagenUrl, producto.getImagenUrl(), "La URL de imagen debe coincidir");
        assertEquals(compania, producto.getCompania(), "La compañía debe coincidir");
        assertEquals(precioUnitario, producto.getPrecioUnitario(), "El precio unitario debe coincidir");
    }

    /**
     * Verifica que los setters y getters funcionan correctamente para todos los atributos.
     */
    @Test
    public void testSettersYGetters() {
        Long id = 1L;
        String articulo = "Cartera";
        String descripcion = "Cartera de alta gama";
        int precio = 1500;
        Categoria categoria = new Categoria(1L, "Mochila", "Productos de mochila");
        int stock = 10;
        String imagenUrl = "http://ejemplo.com/imagen.jpg";
        Compania compania = new Compania(1L, "GC");
        int precioUnitario = 150;

        Producto producto = new Producto(id, articulo, descripcion, precio, categoria, stock, imagenUrl, compania, precioUnitario);

        producto.setArticulo(articulo);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setPrecioUnitario(precioUnitario);
        producto.setCategoria(categoria);
        producto.setStock(stock);
        producto.setImagenUrl(imagenUrl);
        producto.setCompania(compania);

        assertEquals(articulo, producto.getArticulo(), "El artículo debe actualizarse");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe actualizarse");
        assertEquals(precio, producto.getPrecio(), "El precio debe actualizarse");
        assertEquals(precioUnitario, producto.getPrecioUnitario(), "El precio unitario debe actualizarse");
        assertEquals(categoria, producto.getCategoria(), "La categoría debe actualizarse");
        assertEquals(stock, producto.getStock(), "El stock debe actualizarse");
        assertEquals(imagenUrl, producto.getImagenUrl(), "La URL de imagen debe actualizarse");
        assertEquals(compania, producto.getCompania(), "La compañía debe actualizarse");
    }

}
