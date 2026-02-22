package com.store.api.dto.venta;

import java.time.LocalDateTime;

import com.store.api.entity.Producto;

public class VentaUpdateDTO {
    private Long id;
    private Producto producto;
    private Integer cantidad;
    private LocalDateTime fecha;

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
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public void setId(Long id) {
        this.id = id;
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
