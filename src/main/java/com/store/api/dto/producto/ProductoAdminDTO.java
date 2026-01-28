package com.store.api.dto.producto;

import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;

public class ProductoAdminDTO {
    private Long id;
    private String articulo;
    private String descripcion;
    private int stock;
    private int precio;
    private Categoria categoria;
    private String imagenUrl;
    private int precioUnitario;
    private Compania compania;

    public ProductoAdminDTO() {}

    public ProductoAdminDTO(Long id, String articulo, String descripcion, int stock,
                            int precio, Categoria categoria, String imagenUrl,
                            int precioUnitario, Compania compania) {
        this.id = id;
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.precioUnitario = precioUnitario;
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
    public int getPrecioUnitario() {
        return precioUnitario;
    }
    
}
