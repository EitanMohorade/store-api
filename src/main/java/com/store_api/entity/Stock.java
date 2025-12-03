package com.store_api.entity;

public class Stock {
    private Long id;
    private Long productoId;
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
