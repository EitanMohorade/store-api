package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test unitario para la entidad Categoria.
 * 
 * Verifica la construcción, getters y setters de la clase Categoria.
 */
public class CategoriaTest {

    @Test
    public void testConstructorVacio() {
        // Arrange & Act
        Categoria categoria = new Categoria();

        // Assert
        assertNotNull(categoria, "La instancia no debe ser nula");
    }

    @Test
    public void testConstructorConParametros() {
        // Arrange
        Long id = 1L;
        String nombre = "bolsos";
        String descripcion = "accesorios de moda";

        // Act
        Categoria categoria = new Categoria(id, nombre, descripcion);

        // Assert
        assertEquals(id, categoria.getId(), "El ID debe coincidir");
        assertEquals(nombre, categoria.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion, categoria.getDescripcion(), "La descripción debe coincidir");
    }

    @Test
    public void testSettersYGetters() {
        // Arrange
        Categoria categoria = new Categoria();
        String nombre = "bandoleras";
        String descripcion = "bolsos pequeños";

        // Act
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        // Assert
        assertEquals(nombre, categoria.getNombre(), "El nombre debe actualizarse");
        assertEquals(descripcion, categoria.getDescripcion(), "La descripción debe actualizarse");
    }

    @Test
    public void testSetId() {
        // Arrange
        Categoria categoria = new Categoria();
        Long id = 5L;

        // Act
        categoria.setId(id);

        // Assert
        assertEquals(id, categoria.getId(), "El ID debe actualizarse");
    }
}
