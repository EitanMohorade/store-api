package com.store.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Compania;

public interface CompaniaRepository extends JpaRepository<Compania, Long> {
}

