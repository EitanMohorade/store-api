package com.store.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.api.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Verifica si existe un producto con el artículo especificado.
     * 
     * @param articulo Artículo del producto
     * @return true si existe un producto con el artículo, false en caso contrario
     */
    boolean existsByArticulo(String articulo);

}

