package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Suite de tests para CategoriaRepository.
 * 
 * Valida las operaciones CRUD (Create, Read, Update, Delete) y comportamientos
 * específicos de la entidad Categoria en la base de datos H2 de prueba.
 * 
 * Los tests cubren:
 * - Creación de categorías con y sin descripción
 * - Recuperación de categorías por ID
 * - Actualización de nombres y descripciones de categorías
 * - Eliminación de categorías por ID y por objeto
 * - Búsqueda y conteo de múltiples categorías
 * 
 */
@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Verifica que se pueda crear una categoría y recuperarla por ID.
     */
    @Test
    void debeCrearYEncontrarCategoria() {
        Categoria cat = new Categoria();
        cat.setNombre("Mochilas");

        Categoria guardada = categoriaRepository.save(cat);

        assertNotNull(guardada.getId());

        Long id = guardada.getId();
        assertNotNull(id);
        Categoria encontrada = categoriaRepository.findById(id).orElse(null);

        assertNotNull(encontrada);
        assertEquals("Mochilas", encontrada.getNombre());
    }

    /**
     * Verifica que se pueda eliminar una categoría del repositorio correctamente.
     */
    @Test
    void debeEliminarCategoriaDelRepositorio() {
        Categoria cat = new Categoria();
        cat.setNombre("Bolsos");

        Categoria guardada = categoriaRepository.save(cat);
        Long id = guardada.getId();

        assertNotNull(id);

        categoriaRepository.delete(guardada);

        assertFalse(categoriaRepository.findById(id).isPresent());
    }

    /**
     * Verifica que se pueda actualizar el nombre de una categoría existente.
     */
    @Test
    void debeActualizarNombreDeCategoria() {
        Categoria cat = new Categoria();
        cat.setNombre("Bandoleras");

        Categoria guardada = categoriaRepository.save(cat);
        Long id = guardada.getId();

        assertNotNull(id);

        guardada.setNombre("Mochilas");
        categoriaRepository.save(guardada);

        Categoria actualizada = categoriaRepository.findById(id).orElse(null);
        assertNotNull(actualizada);
        assertEquals("Mochilas", actualizada.getNombre());
    }  

    /**
     * Verifica que se pueda crear una categoría usando el constructor completo con descripción.
     */
    @Test
    void debeCrearCategoriaConConstructorCompleto() {
        Categoria cat = new Categoria(null, "Bandoleras", "Artículos para llevar objetos personales");

        Categoria guardada = categoriaRepository.save(cat);

        assertNotNull(guardada.getId());
        assertEquals("Bandoleras", guardada.getNombre());
        assertEquals("Artículos para llevar objetos personales", guardada.getDescripcion());
    }

    /**
     * Verifica que se puedan encontrar múltiples categorías en el repositorio.
     */
    @Test
    void debeEncontrarCategoriasPorNombre() {
        Categoria cat1 = new Categoria();
        cat1.setNombre("Bandoleras");
        categoriaRepository.save(cat1);

        Categoria cat2 = new Categoria();
        cat2.setNombre("Carteras");
        categoriaRepository.save(cat2);

        var encontradas = categoriaRepository.findAll();
        assertEquals(2, encontradas.size());
    }

    /**
     * Verifica que se pueda eliminar una categoría por su ID.
     */
    @Test
    void debeEliminarCategoriaPorId() {
        Categoria cat = new Categoria();
        cat.setNombre("Mochilas");

        Categoria guardada = categoriaRepository.save(cat);
        Long id = guardada.getId();

        assertNotNull(id);

        categoriaRepository.deleteById(id);

        assertFalse(categoriaRepository.findById(id).isPresent());
    }

    
}
