package com.store.api.dto.producto;

import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;

/**
 * DTO administrativo para un producto.
 * Incluye imagenPublicId para operaciones internas de administración.
 */
public class ProductoAdminDTO {

    private Long id;
    private String articulo;
    private String descripcion;
    private int stock;
    private int precio;
    private Categoria categoria;
    private String imagenUrl;
    private String imagenPublicId;
    private int precioUnitario;
    private Compania compania;

    public ProductoAdminDTO() {}

    public ProductoAdminDTO(Long id, String articulo, String descripcion, int stock,
                            int precio, Categoria categoria, String imagenUrl,
                            String imagenPublicId, int precioUnitario, Compania compania) {
        this.id = id;
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.imagenPublicId = imagenPublicId;
        this.precioUnitario = precioUnitario;
        this.compania = compania;
    }

    public Long getId() { return id; }
    public String getArticulo() { return articulo; }
    public String getDescripcion() { return descripcion; }
    public int getStock() { return stock; }
    public int getPrecio() { return precio; }
    public Categoria getCategoria() { return categoria; }
    public String getImagenUrl() { return imagenUrl; }
    public String getImagenPublicId() { return imagenPublicId; }
    public int getPrecioUnitario() { return precioUnitario; }
    public Compania getCompania() { return compania; }
}
