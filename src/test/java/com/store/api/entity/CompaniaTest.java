package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Suite de tests unitarios para la entidad Compania.
 * 
 * Valida la construcción, getters, setters y comportamiento general de la clase Compania,
 * incluyendo inicialización con y sin parámetros, y manipulación de atributos.
 * 
 */
public class CompaniaTest {

    /**
     * Verifica que se pueda crear una instancia de Compania con el constructor vacío.
     */
    @Test
    public void testConstructorVacio() {
        Compania compania = new Compania();

        assertNotNull(compania, "La instancia no debe ser nula");
    }

    /**
     * Verifica que el constructor con parámetros inicialice correctamente los atributos.
     */
    @Test
    public void testConstructorConParametros() {
        Long id = 1L;
        String nombre = "GC";

        Compania compania = new Compania(id, nombre);

        assertEquals(id, compania.getId(), "El ID debe coincidir");
        assertEquals(nombre, compania.getNombre(), "El nombre debe coincidir");
    }

    /**
     * Verifica que los setters y getters funcionan correctamente.
     */
    @Test
    public void testSettersYGetters() {
        Compania compania = new Compania();
        String nombre = "NJ";

        compania.setNombre(nombre);

        assertEquals(nombre, compania.getNombre(), "El nombre debe actualizarse");
    }

    /**
     * Verifica que el ID pueda ser establecido y recuperado correctamente.
     */
    @Test
    public void testSetId() {
        Compania compania = new Compania();
        Long id = 10L;

        compania.setId(id);

        assertEquals(id, compania.getId(), "El ID debe actualizarse");
    }
}
