package com.store.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Compania;

public interface CompaniaRepository extends JpaRepository<Compania, Long> {

    
    boolean existsByNombre(String nombre);

    Optional<Compania> findByNombreIgnoreCase(String nombre);
}

