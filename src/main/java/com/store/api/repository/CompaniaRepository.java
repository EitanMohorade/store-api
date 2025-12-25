package com.store.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Compania;


/**
 * Repositorio para la entidad Compania.
 */
public interface CompaniaRepository extends JpaRepository<Compania, Long> {


    /**
     * Encuentra productos por el ID de su categoría.
     * 
     * @param nombre Nombre de la compañía
     * @return true si existe una compañía con el nombre, false en caso contrario
     */
    boolean existsByNombre(String nombre);

    /**
     * Obtiene una compañía por su nombre.
     * 
     * @param nombre Nombre de la compañía
     * @return Compañía encontrada
     */
    Optional<Compania> findByNombreIgnoreCase(String nombre);

}

