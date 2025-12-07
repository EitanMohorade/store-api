package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Suite de tests unitarios para la entidad Admin.
 * 
 * Valida la construcción, getters, setters y comportamiento general de la clase Admin,
 * incluyendo inicialización con y sin parámetros, y manipulación de atributos.
 * 
 */
public class AdminTest {

    /**
     * Verifica que se pueda crear una instancia de Admin con el constructor vacío.
     */
    @Test
    public void testConstructorVacio() {
        Admin admin = new Admin();

        assertNotNull(admin, "La instancia no debe ser nula");
    }

    /**
     * Verifica que el constructor con parámetros inicialice correctamente los atributos.
     */
    @Test
    public void testConstructorConParametros() {
        Long id = 1L;
        String nombre = "admin_user";
        String password = "encrypted_password";

        Admin admin = new Admin(id, nombre, password);

        assertEquals(id, admin.getId(), "El ID debe coincidir");
        assertEquals(nombre, admin.getNombre(), "El nombre debe coincidir");
        assertEquals(password, admin.getPassword(), "La contraseña debe coincidir");
    }

    /**
     * Verifica que los setters y getters funcionan correctamente.
     */
    @Test
    public void testSettersYGetters() {
        Admin admin = new Admin();
        String nombre = "nuevo_admin";
        String password = "nueva_password_encriptada";

        admin.setNombre(nombre);
        admin.setPassword(password);

        assertEquals(nombre, admin.getNombre(), "El nombre debe actualizarse");
        assertEquals(password, admin.getPassword(), "La contraseña debe actualizarse");
    }

    /**
     * Verifica que el ID pueda ser establecido y recuperado correctamente.
     */
    @Test
    public void testSetId() {
        Admin admin = new Admin();
        Long id = 5L;

        admin.setId(id);

        assertEquals(id, admin.getId(), "El ID debe actualizarse");
    }
}
