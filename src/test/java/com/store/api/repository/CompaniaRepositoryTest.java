package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Compania;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Test de integración para CompaniaRepository.
 * 
 * Verifica las operaciones CRUD en la base de datos para compañías.
 */
@DataJpaTest
public class CompaniaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompaniaRepository companiaRepository;

    private Compania compania;

    @BeforeEach
    public void setUp() {
        compania = new Compania();
        compania.setNombre("GC");
    }

    @Test
    public void testGuardarCompania() {
        // Act
        Compania savedCompania = companiaRepository.save(compania);

        // Assert
        assertNotNull(savedCompania.getId(), "El ID no debe ser nulo después de guardar");
        assertEquals("GC", savedCompania.getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testEncontrarCompaniaPorId() {
        // Arrange
        Compania savedCompania = companiaRepository.save(compania);
        entityManager.flush();

        // Act
        var resultado = companiaRepository.findById(savedCompania.getId());

        // Assert
        assertTrue(resultado.isPresent(), "La compañía debe encontrarse");
        assertEquals("GC", resultado.get().getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testListarTodasCompanias() {
        // Arrange
        companiaRepository.save(compania);
        entityManager.flush();

        // Act
        var companias = companiaRepository.findAll();

        // Assert
        assertTrue(companias.size() > 0, "Debe haber al menos una compañía");
    }

    @Test
    public void testEliminarCompania() {
        // Arrange
        Compania savedCompania = companiaRepository.save(compania);
        entityManager.flush();

        // Act
        companiaRepository.delete(savedCompania);
        entityManager.flush();

        // Assert
        var resultado = companiaRepository.findById(savedCompania.getId());
        assertFalse(resultado.isPresent(), "La compañía debe eliminarse");
    }

    @Test
    public void testActualizarCompania() {
        // Arrange
        Compania savedCompania = companiaRepository.save(compania);
        entityManager.flush();

        // Act
        savedCompania.setNombre("NJ");
        Compania updatedCompania = companiaRepository.save(savedCompania);

        // Assert
        assertEquals("NJ", updatedCompania.getNombre(), "El nombre debe actualizarse");
    }
}
