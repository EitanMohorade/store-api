package com.store.api.dto.venta;

import java.time.LocalDateTime;

import com.store.api.entity.Producto;

public class VentaResponseDTO {
    private Long id;
    private Producto producto;
    private Integer cantidad;
    private LocalDateTime fecha;

    public VentaResponseDTO(Long id, Producto producto, Integer cantidad, LocalDateTime fecha) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }
    public Producto getProducto() {
        return producto;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }

    public Integer getPrecioUnitario() {
        return producto != null ? producto.getPrecioUnitario() : 0;
    }
    public Integer getPrecio() {
        return producto != null ? producto.getPrecio() : 0;
    }
    public Integer getTotalPrecioUnitario() {
        return getPrecioUnitario() * (cantidad != null ? cantidad : 0);
    }
    public Integer getTotalPrecio() {
        return getPrecio() * (cantidad != null ? cantidad : 0);
    }
}
