package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Compania;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Suite de tests para CompaniaRepository.
 * 
 * Valida las operaciones CRUD (Create, Read, Update, Delete) y comportamientos
 * específicos de la entidad Compania en la base de datos H2 de prueba.
 * 
 * Los tests cubren:
 * - Creación de compañías
 * - Recuperación de compañías por ID
 * - Actualización de nombres de compañías
 * - Eliminación de compañías por ID y por objeto
 * - Búsqueda y conteo de múltiples compañías
 * - Validación de compañías con nombres vacíos o muy largos
 * 
 */
@DataJpaTest
public class CompaniaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompaniaRepository companiaRepository;
    
    /**
     * Verifica que se pueda crear una compañía y recuperarla por ID.
     */
    @Test
    void debeCrearYEncontrarCompania() {
        Compania comp = new Compania();
        comp.setNombre("GC");
        
        Compania guardada = companiaRepository.save(comp);
        
        assertNotNull(guardada.getId());
        
        Long id = guardada.getId();
        assertNotNull(id);
        Compania encontrada = companiaRepository.findById(id).orElse(null);
        
        assertNotNull(encontrada);
        assertEquals("GC", encontrada.getNombre());
    }

    /**
     * Verifica que se pueda eliminar una compañía del repositorio correctamente.
     */
    @Test
    void debeEliminarCompaniaDelRepositorio() {
        Compania comp = new Compania();
        comp.setNombre("GC");

        Compania guardada = companiaRepository.save(comp);
        Long id = guardada.getId();

        assertNotNull(id);
        companiaRepository.deleteById(id);

        assertFalse(companiaRepository.findById(id).isPresent());
    }
    
    /**
     * Verifica que se pueda actualizar el nombre de una compañía existente.
     */
    @Test
    void debeActualizarNombreDeCompania() {
        Compania comp = new Compania();
        comp.setNombre("GC");

        Compania guardada = companiaRepository.save(comp);
        Long id = guardada.getId();

        assertNotNull(id);

        guardada.setNombre("NJ");
        companiaRepository.save(guardada);

        Compania actualizada = companiaRepository.findById(id).orElse(null);

        assertNotNull(actualizada);
        assertEquals("NJ", actualizada.getNombre());
    }

    /**
     * Verifica que se pueda crear una compañía usando el constructor completo.
     */
    @Test
    void debeCrearCompaniaConConstructorCompleto() {
        Compania comp = new Compania(null, "THAT");

        Compania guardada = companiaRepository.save(comp);

        assertNotNull(guardada.getId());
        assertEquals("THAT", guardada.getNombre());
    }

    /**
     * Verifica que se puedan encontrar múltiples compañías en el repositorio.
     */
    @Test
    void debeEncontrarCompaniasPorNombre() {
        Compania comp1 = new Compania();
        comp1.setNombre("GC");
        companiaRepository.save(comp1);

        Compania comp2 = new Compania();
        comp2.setNombre("NJ");
        companiaRepository.save(comp2);

        Compania comp3 = new Compania();
        comp3.setNombre("THAT");
        companiaRepository.save(comp3);

        var encontradas = companiaRepository.findAll();
        assertEquals(3, encontradas.size());
    }

    /**
     * Verifica que se pueda eliminar una compañía por su ID.
     */
    @Test
    void debeEliminarCompaniaPorId() {
        Compania comp = new Compania();
        comp.setNombre("NJ");

        Compania guardada = companiaRepository.save(comp);
        Long id = guardada.getId();

        assertNotNull(id);

        companiaRepository.deleteById(id);

        assertFalse(companiaRepository.findById(id).isPresent());
    }

    /**
     * Verifica que una búsqueda de una compañía inexistente retorne vacío.
     */
    @Test
    void debeVerificarQueCompaniaNoExisteAntesDeGuardar() {
        Long idInexistente = 99999L;

        var resultado = companiaRepository.findById(idInexistente);

        assertFalse(resultado.isPresent());
    }

    /**
     * Verifica que se puedan guardar y recuperar múltiples compañías en el repositorio.
     */
    @Test
    void debeGuardarYRecuperarMultiplesCompanias() {
        Compania comp1 = new Compania();
        comp1.setNombre("NJ");
        Compania guardada1 = companiaRepository.save(comp1);

        Compania comp2 = new Compania();
        comp2.setNombre("THAT");
        Compania guardada2 = companiaRepository.save(comp2);

        Compania comp3 = new Compania();
        comp3.setNombre("GC");
        Compania guardada3 = companiaRepository.save(comp3);

        assertNotNull(guardada1.getId());
        assertNotNull(guardada2.getId());
        assertNotNull(guardada3.getId());

        Compania encontrada1 = companiaRepository.findById(guardada1.getId()).orElse(null);
        Compania encontrada2 = companiaRepository.findById(guardada2.getId()).orElse(null);
        Compania encontrada3 = companiaRepository.findById(guardada3.getId()).orElse(null);

        assertEquals("NJ", encontrada1.getNombre());
        assertEquals("THAT", encontrada2.getNombre());
        assertEquals("GC", encontrada3.getNombre());
    }

    /**
     * Verifica que se pueda actualizar el nombre de una compañía 3 veces.
     */
    @Test
    void debeActualizarTresVecesNombreDeCompania() {
        Compania comp = new Compania();
        comp.setNombre("GC");

        Compania guardada = companiaRepository.save(comp);

        guardada.setNombre("THAT");
        companiaRepository.save(guardada);

        guardada.setNombre("NJ");
        companiaRepository.save(guardada);

        guardada.setNombre("CHINO");
        Compania actualizada = companiaRepository.save(guardada);

        assertEquals("CHINO", actualizada.getNombre());
    }

    @Test
    void debePermitirCompaniaConNombreVacio() {
        Compania comp = new Compania();
        comp.setNombre("");

        Compania guardada = companiaRepository.save(comp);

        assertNotNull(guardada.getId());
        assertEquals("", guardada.getNombre());
    }

    @Test
    void debePermitirCompaniaConNombreLargo() {
        String nombreLargo = "Esta es una compañía con un nombre muy largo que podría tener muchos caracteres en el sistema";
        Compania comp = new Compania();
        comp.setNombre(nombreLargo);

        Compania guardada = companiaRepository.save(comp);

        assertNotNull(guardada.getId());
        assertEquals(nombreLargo, guardada.getNombre());
    }

    @Test
    void debeEliminarCompaniaCompleta() {
        Compania comp = new Compania();
        comp.setNombre("GC");

        Compania guardada = companiaRepository.save(comp);
        Long id = guardada.getId();

        assertNotNull(id);
        companiaRepository.delete(guardada);

        assertFalse(companiaRepository.findById(id).isPresent());
    }
}
