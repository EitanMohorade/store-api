package com.store.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test unitario para la entidad Admin.
 * 
 * Verifica la construcción, getters y setters de la clase Admin.
 */
public class AdminTest {

    @Test
    public void testConstructorVacio() {
        // Arrange & Act
        Admin admin = new Admin();

        // Assert
        assertNotNull(admin, "La instancia no debe ser nula");
    }

    @Test
    public void testConstructorConParametros() {
        // Arrange
        Long id = 1L;
        String nombre = "admin_user";
        String password = "encrypted_password";

        // Act
        Admin admin = new Admin(id, nombre, password);

        // Assert
        assertEquals(id, admin.getId(), "El ID debe coincidir");
        assertEquals(nombre, admin.getNombre(), "El nombre debe coincidir");
        assertEquals(password, admin.getPassword(), "La contraseña debe coincidir");
    }

    @Test
    public void testSettersYGetters() {
        // Arrange
        Admin admin = new Admin();
        String nombre = "nuevo_admin";
        String password = "nueva_password_encriptada";

        // Act
        admin.setNombre(nombre);
        admin.setPassword(password);

        // Assert
        assertEquals(nombre, admin.getNombre(), "El nombre debe actualizarse");
        assertEquals(password, admin.getPassword(), "La contraseña debe actualizarse");
    }

    @Test
    public void testSetId() {
        // Arrange
        Admin admin = new Admin();
        Long id = 5L;

        // Act
        admin.setId(id);

        // Assert
        assertEquals(id, admin.getId(), "El ID debe actualizarse");
    }
}
