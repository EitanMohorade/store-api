package com.store.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    boolean existsByArticulo(String articulo);

}

