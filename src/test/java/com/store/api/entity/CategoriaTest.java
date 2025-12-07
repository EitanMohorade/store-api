package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Suite de tests unitarios para la entidad Categoria.
 * 
 * Valida la construcción, getters, setters y comportamiento general de la clase Categoria,
 * incluyendo inicialización con y sin parámetros, y manipulación de atributos.
 * 
 */
public class CategoriaTest {

    /**
     * Verifica que se pueda crear una instancia de Categoria con el constructor vacío.
     */
    @Test
    public void testConstructorVacio() {
        Categoria categoria = new Categoria();

        assertNotNull(categoria, "La instancia no debe ser nula");
    }

    /**
     * Verifica que el constructor con parámetros inicialice correctamente los atributos.
     */
    @Test
    public void testConstructorConParametros() {
        Long id = 1L;
        String nombre = "bolsos";
        String descripcion = "accesorios de moda";

        Categoria categoria = new Categoria(id, nombre, descripcion);

        assertEquals(id, categoria.getId(), "El ID debe coincidir");
        assertEquals(nombre, categoria.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion, categoria.getDescripcion(), "La descripción debe coincidir");
    }

    /**
     * Verifica que los setters y getters funcionan correctamente.
     */
    @Test
    public void testSettersYGetters() {
        Categoria categoria = new Categoria();
        String nombre = "bandoleras";
        String descripcion = "bolsos pequeños";

        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        assertEquals(nombre, categoria.getNombre(), "El nombre debe actualizarse");
        assertEquals(descripcion, categoria.getDescripcion(), "La descripción debe actualizarse");
    }

    /**
     * Verifica que el ID pueda ser establecido y recuperado correctamente.
     */
    @Test
    public void testSetId() {
        Categoria categoria = new Categoria();
        Long id = 5L;

        categoria.setId(id);

        assertEquals(id, categoria.getId(), "El ID debe actualizarse");
    }
}
