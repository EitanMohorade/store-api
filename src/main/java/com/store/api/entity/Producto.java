package com.store.api.entity;

public class Producto {
    private Long id;
    private String articulo;
    private String descripcion;
    private int precio;
    private Categoria categoria;
    private Stock stock;
    private String imagenUrl;
    private Compania compania;

    public Producto() {}

    public Producto(Long id, String articulo, String descripcion, int precio, Categoria categoria, Stock stock, String imagenUrl, Compania compania) {
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

    public String getarticulo() { return articulo; }
    public void setarticulo(String articulo) { this.articulo = articulo; }

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
