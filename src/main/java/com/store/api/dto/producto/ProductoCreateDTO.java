package com.store.api.dto.producto;

import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;

public class ProductoCreateDTO {
    private Long id;
    private String articulo;
    private String descripcion;
    private int stock;
    private int precio;
    private Categoria categoria;
    private String imagenUrl;
    private int precioUnitario;
    private Compania compania;

     public Compania getCompania() {
        return compania;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }
    public String getArticulo() {
        return articulo;
    }
    public Long getId() {
        return id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public int getStock() {
        return stock;
    }
    public int getPrecio() {
        return precio;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    public void setCompania(Compania compania) {
        this.compania = compania;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
