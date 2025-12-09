package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa un producto en el sistema.
 * 
 * Un producto es un artículo de inventario que pertenece a una categoría,
 * tiene asociado un stock, preio y precio unitario y pertenece a una compañía.
 */
@Entity
public class Producto {

    /** Identificador único del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre o código del artículo. */
    private String articulo;
    
    /** Descripción detallada del producto. */
    private String descripcion;
    
    /** Precio unitario del producto en unidades monetarias. */
    private int precio;

    /** Precio cliente del producto. */
    private int precioUnitario;

    /** Categoría a la que pertenece el producto. */
    @ManyToOne
    private Categoria categoria;

    /** Stock del producto. */
    private int stock;

    /** URL de la imagen del producto. */
    private String imagenUrl;

    /** Compañía propietaria del producto. */
    @ManyToOne
    private Compania compania;

    public Producto() {}

    public Producto(Long id, String articulo, String descripcion, int precio, Categoria categoria,
                    int stock, String imagenUrl, Compania compania, int precioUnitario) {
        this.id = id;
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.compania = compania;
        this.precioUnitario = precioUnitario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getArticulo() { return articulo; }
    public void setArticulo(String articulo) { this.articulo = articulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }

    public int getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(int precioUnitario) { this.precioUnitario = precioUnitario; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Compania getCompania() { return compania; }
    public void setCompania(Compania compania) { this.compania = compania; }
}
