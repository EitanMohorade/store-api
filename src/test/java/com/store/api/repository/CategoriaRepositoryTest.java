package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Test de integración para CategoriaRepository.
 * 
 * Verifica las operaciones CRUD en la base de datos para categorías.
 */
@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        categoria = new Categoria();
        categoria.setNombre("cartera");
        categoria.setDescripcion("bolsos y carteras");
    }

    @Test
    public void testGuardarCategoria() {
        // Act
        Categoria savedCategoria = categoriaRepository.save(categoria);

        // Assert
        assertNotNull(savedCategoria.getId(), "El ID no debe ser nulo después de guardar");
        assertEquals("cartera", savedCategoria.getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testEncontrarCategoriaPorId() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria);
        entityManager.flush();

        // Act
        var resultado = categoriaRepository.findById(savedCategoria.getId());

        // Assert
        assertTrue(resultado.isPresent(), "La categoría debe encontrarse");
        assertEquals("cartera", resultado.get().getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testListarTodasCategorias() {
        // Arrange
        categoriaRepository.save(categoria);
        entityManager.flush();

        // Act
        var categorias = categoriaRepository.findAll();

        // Assert
        assertTrue(categorias.size() > 0, "Debe haber al menos una categoría");
    }

    @Test
    public void testEliminarCategoria() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria);
        entityManager.flush();

        // Act
        categoriaRepository.delete(savedCategoria);
        entityManager.flush();

        // Assert
        var resultado = categoriaRepository.findById(savedCategoria.getId());
        assertFalse(resultado.isPresent(), "La categoría debe eliminarse");
    }

    @Test
    public void testActualizarCategoria() {
        // Arrange
        Categoria savedCategoria = categoriaRepository.save(categoria);
        entityManager.flush();

        // Act
        savedCategoria.setNombre("mochilas");
        Categoria updatedCategoria = categoriaRepository.save(savedCategoria);

        // Assert
        assertEquals("mochilas", updatedCategoria.getNombre(), "El nombre debe actualizarse");
    }
}
