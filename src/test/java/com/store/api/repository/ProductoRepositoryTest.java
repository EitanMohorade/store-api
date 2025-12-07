package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Producto;
import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Test de integración para ProductoRepository.
 * 
 * Verifica las operaciones CRUD en la base de datos para productos.
 */
@DataJpaTest
public class ProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    private Producto producto;
    private Categoria categoria;
    private Compania compania;

    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
        categoria = new Categoria();
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Productos electrónicos");
        entityManager.persistAndFlush(categoria);

        compania = new Compania();
        compania.setNombre("CompañiaTest");
        entityManager.persistAndFlush(compania);

        producto = new Producto();
        producto.setArticulo("Laptop");
        producto.setDescripcion("Laptop de prueba");
        producto.setPrecio(1000);
        producto.setCategoria(categoria);
        producto.setCompania(compania);
    }

    @Test
    public void testGuardarProducto() {
        // Act
        Producto savedProducto = productoRepository.save(producto);

        // Assert
        assertNotNull(savedProducto.getId(), "El ID no debe ser nulo después de guardar");
        assertEquals("Laptop", savedProducto.getArticulo(), "El artículo debe coincidir");
    }

    @Test
    public void testEncontrarProductoPorId() {
        // Arrange
        Producto savedProducto = productoRepository.save(producto);
        entityManager.flush();

        // Act
        var resultado = productoRepository.findById(savedProducto.getId());

        // Assert
        assertTrue(resultado.isPresent(), "El producto debe encontrarse");
        assertEquals("Laptop", resultado.get().getArticulo(), "El artículo debe coincidir");
    }

    @Test
    public void testListarTodosProductos() {
        // Arrange
        productoRepository.save(producto);
        entityManager.flush();

        // Act
        var productos = productoRepository.findAll();

        // Assert
        assertTrue(productos.size() > 0, "Debe haber al menos un producto");
    }

    @Test
    public void testEliminarProducto() {
        // Arrange
        Producto savedProducto = productoRepository.save(producto);
        entityManager.flush();

        // Act
        productoRepository.delete(savedProducto);
        entityManager.flush();

        // Assert
        var resultado = productoRepository.findById(savedProducto.getId());
        assertFalse(resultado.isPresent(), "El producto debe eliminarse");
    }
}
