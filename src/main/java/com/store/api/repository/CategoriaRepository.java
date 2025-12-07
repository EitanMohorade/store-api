package com.store.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

