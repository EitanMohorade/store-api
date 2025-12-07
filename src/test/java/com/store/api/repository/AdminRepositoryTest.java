package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Test de integración para AdminRepository.
 * 
 * Verifica las operaciones CRUD en la base de datos para administradores.
 */
@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    private Admin admin;

    @BeforeEach
    public void setUp() {
        admin = new Admin();
        admin.setNombre("admin_test");
        admin.setPassword("encrypted_pass_123");
    }

    @Test
    public void testGuardarAdmin() {
        // Act
        Admin savedAdmin = adminRepository.save(admin);

        // Assert
        assertNotNull(savedAdmin.getId(), "El ID no debe ser nulo después de guardar");
        assertEquals("admin_test", savedAdmin.getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testEncontrarAdminPorId() {
        // Arrange
        Admin savedAdmin = adminRepository.save(admin);
        entityManager.flush();

        // Act
        var resultado = adminRepository.findById(savedAdmin.getId());

        // Assert
        assertTrue(resultado.isPresent(), "El admin debe encontrarse");
        assertEquals("admin_test", resultado.get().getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testListarTodosAdmins() {
        // Arrange
        adminRepository.save(admin);
        entityManager.flush();

        // Act
        var admins = adminRepository.findAll();

        // Assert
        assertTrue(admins.size() > 0, "Debe haber al menos un admin");
    }

    @Test
    public void testEliminarAdmin() {
        // Arrange
        Admin savedAdmin = adminRepository.save(admin);
        entityManager.flush();

        // Act
        adminRepository.delete(savedAdmin);
        entityManager.flush();

        // Assert
        var resultado = adminRepository.findById(savedAdmin.getId());
        assertFalse(resultado.isPresent(), "El admin debe eliminarse");
    }

    @Test
    public void testActualizarAdmin() {
        // Arrange
        Admin savedAdmin = adminRepository.save(admin);
        entityManager.flush();

        // Act
        savedAdmin.setNombre("admin_actualizado");
        Admin updatedAdmin = adminRepository.save(savedAdmin);

        // Assert
        assertEquals("admin_actualizado", updatedAdmin.getNombre(), "El nombre debe actualizarse");
    }
}
