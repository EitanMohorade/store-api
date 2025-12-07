package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Suite de tests para AdminRepository.
 * 
 * Valida las operaciones CRUD (Create, Read, Update, Delete) y comportamientos
 * específicos de la entidad Admin en la base de datos H2 de prueba.
 * 
 * Los tests cubren:
 * - Creación de administradores con nombre y contraseña
 * - Recuperación de administradores por ID
 * - Actualización de nombres de administradores
 * - Actualización de contraseñas de administradores
 * - Eliminación de administradores
 * - Creación con constructor completo
 *
 */
@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Verifica que se pueda crear un administrador y recuperarlo por ID.
     */
    @Test
    void debeCrearYEncontrarAdmin() {
        Admin admin = new Admin();
        admin.setNombre("Admin1");
        admin.setPassword("dsadas@*123");
        Admin guardado = adminRepository.save(admin);
        assertNotNull(guardado.getId());
        Long id = guardado.getId();

        assertNotNull(id);

        Admin encontrado = adminRepository.findById(id).orElse(null);
        assertNotNull(encontrado);
        assertEquals("Admin1", encontrado.getNombre());
    }

    /**
     * Verifica que se pueda eliminar un administrador del repositorio correctamente.
     */
    @Test
    void debeEliminarAdminDelRepositorio() {
        Admin admin = new Admin();
        admin.setNombre("AdminAeliminar");
        admin.setPassword("123eliminar@");
        Admin guardado = adminRepository.save(admin);
        Long id = guardado.getId();

        assertNotNull(id);

        assertNotNull(adminRepository.findById(id).orElse(null));
        adminRepository.deleteById(id);
        assertNull(adminRepository.findById(id).orElse(null));
    }

    /**
     * Verifica que se pueda actualizar el nombre de un administrador existente.
     */
    @Test
    void debeActualizarNombreDeAdminEnElRepositorio() {
        Admin admin = new Admin();
        admin.setNombre("AdminAActualizar");
        admin.setPassword("actualizar@123");
        Admin guardado = adminRepository.save(admin);
        Long id = guardado.getId();

        assertNotNull(id);

        Admin aActualizar = adminRepository.findById(id).orElse(null);
        assertNotNull(aActualizar);

        aActualizar.setNombre("AdminActualizado");
        adminRepository.save(aActualizar);

        Admin actualizado = adminRepository.findById(id).orElse(null);
        assertNotNull(actualizado);

        assertEquals("AdminActualizado", actualizado.getNombre());
    }

    /**
     * Verifica que se pueda actualizar la contraseña de un administrador existente.
     */
    @Test
    void debeActualizarPasswordDeAdminEnElRepositorio() {
        Admin admin = new Admin();
        admin.setNombre("admin");
        admin.setPassword("viejaContraseña@123");
        Admin guardado = adminRepository.save(admin);
        Long id = guardado.getId();

        assertNotNull(id);

        Admin aActualizar = adminRepository.findById(id).orElse(null);
        assertNotNull(aActualizar);

        aActualizar.setPassword("nuevaContraseña@123");
        adminRepository.save(aActualizar);

        Admin actualizado = adminRepository.findById(id).orElse(null);
        assertNotNull(actualizado);

        assertEquals("nuevaContraseña@123", actualizado.getPassword());
    }

    /**
     * Verifica que se pueda crear un administrador usando el constructor completo.
     */
    @Test
    void debeCrearAdminConConstructorCompleto() {
        Admin admin = new Admin(null, "AdminCompleto", "completo@123");
        Admin guardado = adminRepository.save(admin);
        assertNotNull(guardado.getId());
        Long id = guardado.getId();

        assertNotNull(id);

        Admin encontrado = adminRepository.findById(id).orElse(null);
        assertNotNull(encontrado);
        assertEquals("AdminCompleto", encontrado.getNombre());
        assertEquals("completo@123", encontrado.getPassword());
    }

    /**
     * Verifica que no se pueda crear un administrador sin nombre.
     */
    @Test
    void debeFallarAlCrearAdminSinNombre() {
        Admin admin = new Admin();
        admin.setPassword("sinNombre@123");

        assertThrows(DataIntegrityViolationException.class, () -> {
            adminRepository.save(admin);
            entityManager.flush();
        });
    }

    /**
     * Verifica que no se pueda crear un administrador sin contraseña.
     */
    @Test
    void debeFallarAlCrearAdminSinPassword() {
        Admin admin = new Admin();
        admin.setNombre("sinPassword@123");

        assertThrows(DataIntegrityViolationException.class, () -> {
            adminRepository.save(admin);
            entityManager.flush();
        });
    }

}
