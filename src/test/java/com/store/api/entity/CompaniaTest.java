package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test unitario para la entidad Compania.
 * 
 * Verifica la construcción, getters y setters de la clase Compania.
 */
public class CompaniaTest {

    @Test
    public void testConstructorVacio() {
        // Arrange & Act
        Compania compania = new Compania();

        // Assert
        assertNotNull(compania, "La instancia no debe ser nula");
    }

    @Test
    public void testConstructorConParametros() {
        // Arrange
        Long id = 1L;
        String nombre = "GC";

        // Act
        Compania compania = new Compania(id, nombre);

        // Assert
        assertEquals(id, compania.getId(), "El ID debe coincidir");
        assertEquals(nombre, compania.getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testSettersYGetters() {
        // Arrange
        Compania compania = new Compania();
        String nombre = "NJ";

        // Act
        compania.setNombre(nombre);

        // Assert
        assertEquals(nombre, compania.getNombre(), "El nombre debe actualizarse");
    }

    @Test
    public void testSetId() {
        // Arrange
        Compania compania = new Compania();
        Long id = 10L;

        // Act
        compania.setId(id);

        // Assert
        assertEquals(id, compania.getId(), "El ID debe actualizarse");
    }
}
