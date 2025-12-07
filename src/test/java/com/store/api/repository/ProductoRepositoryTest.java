package com.store.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.store.api.entity.Producto;
import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Suite de tests para ProductoRepository.
 * 
 * Valida las operaciones CRUD (Create, Read, Update, Delete) y comportamientos
 * específicos de la entidad Producto en la base de datos H2 de prueba.
 * 
 * Los tests cubren:
 * - Creación de productos con y sin relaciones (Categoría, Compañía)
 * - Lectura/recuperación de productos por ID
 * - Actualización de atributos de productos
 * - Eliminación de productos
 * - Búsqueda de productos por categoría
 * - Validación de valores límite (stock cero, precios negativos)
 * 
 */
@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Verifica que se pueda crear y recuperar una cartera con todos sus atributos
     * (categoría, compañía, descripción, precio e imagen).
     */
    @Test
    void debeCrearCarteraConCategoriaCompaniaDescripcionPrecioEImagen() {
        Categoria cat = new Categoria();
        cat.setNombre("Carteras");
        entityManager.persist(cat);

        Compania comp = new Compania();
        comp.setNombre("THAT Company");
        entityManager.persist(comp);

        Producto cartiera = new Producto();
        cartiera.setArticulo("2145");
        cartiera.setDescripcion("Cartera impermeable de nylon");
        cartiera.setPrecio(250);
        cartiera.setCategoria(cat);
        cartiera.setCompania(comp);
        cartiera.setImagenUrl("http://img.com/cartera.jpg");

        Producto guardado = productoRepository.save(cartiera);

        assertNotNull(guardado.getId());

        Long id = guardado.getId();
        assertNotNull(id);
        Producto encontrado = productoRepository.findById(id).orElse(null);

        assertNotNull(encontrado);
        assertEquals("2145", encontrado.getArticulo());
    }

    /**
     * Verifica que se pueda eliminar una bandolera del repositorio correctamente.
     */
    @Test
    void debeEliminarBandelaDelRepositorio() {
        Producto bandela = new Producto();
        bandela.setArticulo("S-552");
        bandela.setDescripcion("Bandolera de ecocuero");
        bandela.setPrecio(120);

        Producto guardado = productoRepository.save(bandela);
        Long id = guardado.getId();

        productoRepository.delete(guardado);

        assertNotNull(id);
        assertFalse(productoRepository.findById(id).isPresent());
    }

    /**
     * Verifica que se pueda crear una mochila con stock y recuperarlo correctamente.
     */
    @Test
    void debeCrearMochilaConeStockCorrecto() {
        Producto mochila = new Producto();
        mochila.setArticulo("953");
        mochila.setDescripcion("Mochila escolar");
        mochila.setPrecio(85);
        mochila.setStock(150);

        Producto guardado = productoRepository.save(mochila);

        assertNotNull(guardado.getId());
        assertEquals(150, guardado.getStock());
    }

    /**
     * Verifica que se pueda crear una cartera usando el constructor completo.
     */
    @Test
    void debeCrearCarteraConConstructorCompleto() {
        Categoria cat = new Categoria();
        cat.setNombre("Carteras Deportivas");
        entityManager.persist(cat);

        Compania comp = new Compania();
        comp.setNombre("THAT");
        entityManager.persist(comp);

        Producto cartera = new Producto(null, "699", "Cartera deportiva resistente al agua", 95, cat, 75, "http://img.com/cartera-sport.jpg", comp);

        Producto guardado = productoRepository.save(cartera);

        assertNotNull(guardado.getId());
        assertEquals("699", guardado.getArticulo());
        assertEquals("Cartera deportiva resistente al agua", guardado.getDescripcion());
        assertEquals(95, guardado.getPrecio());
        assertEquals(75, guardado.getStock());
        assertEquals("http://img.com/cartera-sport.jpg", guardado.getImagenUrl());
        assertEquals("THAT", guardado.getCompania().getNombre());
        assertEquals("Carteras Deportivas", guardado.getCategoria().getNombre());
    }

    /**
     * Verifica que se puedan actualizar los atributos de una bandolera existente.
     */
    @Test
    void debeActualizarBandelaExistente() {
        Producto bandela = new Producto();
        bandela.setArticulo("S-774");
        bandela.setDescripcion("Bandolera original");
        bandela.setPrecio(100);
        bandela.setStock(30);

        Producto guardado = productoRepository.save(bandela);

        guardado.setDescripcion("Bandolera actualizada");
        guardado.setPrecio(125);
        guardado.setStock(50);

        Producto actualizado = productoRepository.save(guardado);

        assertEquals("Bandolera actualizada", actualizado.getDescripcion());
        assertEquals(125, actualizado.getPrecio());
        assertEquals(50, actualizado.getStock());
    }

    /**
     * Verifica que se puedan encontrar todas las mochilas que pertenecen a una categoría específica.
     */
    @Test
    void debeEncontrarMochilasDeCategoria() {
        Categoria cat = new Categoria();
        cat.setNombre("Mochilas");
        entityManager.persist(cat);

        Producto mochila1 = new Producto();
        mochila1.setArticulo("932");
        mochila1.setCategoria(cat);
        mochila1.setPrecio(75);
        productoRepository.save(mochila1);

        Producto mochila2 = new Producto();
        mochila2.setArticulo("215");
        mochila2.setCategoria(cat);
        mochila2.setPrecio(180);
        productoRepository.save(mochila2);

        Producto cartera = new Producto();
        cartera.setArticulo("123");
        cartera.setPrecio(120);
        productoRepository.save(cartera);

        long countMochilas = productoRepository.findAll().stream()
            .filter(prod -> prod.getCategoria() != null && prod.getCategoria().getId().equals(cat.getId()))
            .count();

        assertEquals(2, countMochilas);
    }

    /**
     * Verifica que se pueda crear una bandolera sin categoría asignada.
     */
    @Test
    void debeCrearBandelaSinCategoria() {
        Producto bandela = new Producto();
        bandela.setArticulo("924");
        bandela.setDescripcion("Bandolera sin categoría");
        bandela.setPrecio(65);

        Producto guardado = productoRepository.save(bandela);

        assertNotNull(guardado.getId());
        assertNull(guardado.getCategoria());
        assertEquals("924", guardado.getArticulo());
    }

    /**
     * Verifica que se pueda crear una mochila sin compañía asignada.
     */
    @Test
    void debeCrearMochilaSinCompania() {
        Producto mochila = new Producto();
        mochila.setArticulo("9211");
        mochila.setDescripcion("Mochila sin compañía asignada");
        mochila.setPrecio(90);

        Producto guardado = productoRepository.save(mochila);

        assertNotNull(guardado.getId());
        assertNull(guardado.getCompania());
        assertEquals("9211", guardado.getArticulo());
    }

    /**
     * Verifica que se puedan obtener todos los atributos de una cartera recuperada de la base de datos.
     */
    @Test
    void debeObtenerTodosLosDatosDeCartera() {
        Categoria cat = new Categoria();
        cat.setNombre("Carteras Premium");
        entityManager.persist(cat);

        Compania comp = new Compania();
        comp.setNombre("GC");
        entityManager.persist(comp);

        Producto cartera = new Producto();
        cartera.setArticulo("2");
        cartera.setDescripcion("Cartera premium");
        cartera.setPrecio(450);
        cartera.setStock(10);
        cartera.setImagenUrl("http://img.com/cartera-luxury.jpg");
        cartera.setCategoria(cat);
        cartera.setCompania(comp);

        Producto guardado = productoRepository.save(cartera);

        Long id = guardado.getId();
        assertNotNull(id);
        Producto encontrado = productoRepository.findById(id).orElse(null);

        assertNotNull(encontrado);
        assertEquals("2", encontrado.getArticulo());
        assertEquals("Cartera premium", encontrado.getDescripcion());
        assertEquals(450, encontrado.getPrecio());
        assertEquals(10, encontrado.getStock());
        assertEquals("http://img.com/cartera-luxury.jpg", encontrado.getImagenUrl());
        assertNotNull(encontrado.getCategoria());
        assertNotNull(encontrado.getCompania());
    }

    /**
     * Verifica que se puedan encontrar múltiples tipos de productos (mochilas, bandoleras, carteras).
     */
    @Test
    void debeEncontrarVariosTiposDeProductos() {
        Producto mochila = new Producto();
        mochila.setArticulo("MOCH-GENERAL");
        mochila.setPrecio(110);
        productoRepository.save(mochila);

        Producto bandela = new Producto();
        bandela.setArticulo("BAND-GENERAL");
        bandela.setPrecio(80);
        productoRepository.save(bandela);

        Producto cartera = new Producto();
        cartera.setArticulo("CART-GENERAL");
        cartera.setPrecio(95);
        productoRepository.save(cartera);

        var todos = productoRepository.findAll();
        assertTrue(todos.size() >= 3);
    }

    /**
     * Verifica que se pueda eliminar una bandolera por su ID.
     */
    @Test
    void debeEliminarBandelarPorId() {
        Producto bandela = new Producto();
        bandela.setArticulo("BAND-ELIMINAR");
        bandela.setPrecio(70);

        Producto guardado = productoRepository.save(bandela);
        Long id = guardado.getId();

        assertNotNull(id);

        productoRepository.deleteById(id);

        assertFalse(productoRepository.findById(id).isPresent());
    }

    /**
     * Verifica que se permita crear una mochila con stock en cero.
     */
    @Test
    void debePermitirMochilaConStockCero() {
        Producto mochila = new Producto();
        mochila.setArticulo("MOCH-AGOTADA");
        mochila.setStock(0);
        mochila.setPrecio(100);

        Producto guardado = productoRepository.save(mochila);

        assertEquals(0, guardado.getStock());
    }

    /**
     * Verifica que se permita crear una cartera con precio negativo (descuento).
     */
    @Test
    void debePermitirCarteraConPrecioNegativo() {
        Producto cartera = new Producto();
        cartera.setArticulo("CART-DESCUENTO");
        cartera.setPrecio(-30);

        Producto guardado = productoRepository.save(cartera);

        assertEquals(-30, guardado.getPrecio());
    }
}