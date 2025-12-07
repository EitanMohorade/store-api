package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa el stock (cantidad en inventario) de un producto.
 * 
 * Mantiene registro de la cantidad disponible de cada producto en el sistema.
 */
@Entity
public class Stock {

    /** Identificador único del registro de stock. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID del producto asociado a este stock. */
    private Long productoId;
    
    /** Cantidad disponible en inventario. */
    private Integer cantidad;

    public Stock() {}

    public Stock(Long id, Long productoId, Integer cantidad) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
