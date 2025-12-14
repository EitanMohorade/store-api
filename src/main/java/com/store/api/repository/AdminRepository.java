package com.store.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByNombre(String nombre);
}