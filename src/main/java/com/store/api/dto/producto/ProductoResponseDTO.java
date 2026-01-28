package com.store.api.dto.producto;

import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;

public class ProductoResponseDTO {
    private Long id;
    private String articulo;
    private String descripcion;
    private int stock;
    private int precio;
    private Categoria categoria;
    private String imagenUrl;
    private Compania compania;

    public ProductoResponseDTO() {}

    public ProductoResponseDTO(Long id, String articulo, String descripcion, int stock,
                               int precio, Categoria categoria, String imagenUrl, Compania compania) {
        this.id = id;
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.compania = compania;
    }

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


}
