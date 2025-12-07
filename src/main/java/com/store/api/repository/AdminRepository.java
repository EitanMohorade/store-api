package com.store.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}