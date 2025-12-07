package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa un producto en el sistema.
 * 
 * Un producto es un artículo de inventario que pertenece a una categoría,
 * tiene asociado un stock, y pertenece a una compañía.
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

    /** Categoría a la que pertenece el producto. */
    @ManyToOne
    private Categoria categoria;

    /** Stock asociado al producto. */
    @OneToOne(cascade = CascadeType.ALL)
    private Stock stock;

    /** URL de la imagen del producto. */
    private String imagenUrl;

    /** Compañía propietaria del producto. */
    @ManyToOne
    private Compania compania;

    public Producto() {}

    public Producto(Long id, String articulo, String descripcion, int precio, Categoria categoria,
                    Stock stock, String imagenUrl, Compania compania) {
        this.id = id;
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.compania = compania;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getArticulo() { return articulo; }
    public void setArticulo(String articulo) { this.articulo = articulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Compania getCompania() { return compania; }
    public void setCompania(Compania compania) { this.compania = compania; }
}
