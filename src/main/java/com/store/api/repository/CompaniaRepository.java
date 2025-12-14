package com.store.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Compania;

public interface CompaniaRepository extends JpaRepository<Compania, Long> {

    /**
     * Encuentra una compañía por su nombre, ignorando mayúsculas y minúsculas.
     * 
     * @param nombre Nombre de la compañía
     * @return Compañía encontrada envuelta en Optional
     */
    Optional<Compania> findByNombreIgnoreCase(String nombre);

}

