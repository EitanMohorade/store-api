package com.store.api.dto.producto;

import jakarta.validation.constraints.*;
import com.store.api.entity.Categoria;
import com.store.api.entity.Compania;

/**
 * DTO para la actualización de un producto.
 * La imagen se recibe como MultipartFile en el Controller, no en este DTO.
 * Si no se envía imagen nueva, se conserva la imagen existente.
 */
public class ProductoUpdateDTO {

    @NotBlank(message = "El artículo no puede estar vacío")
    private String articulo;

    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @Min(value = 0, message = "El precio no puede ser negativo")
    private int precio;

    private Categoria categoria;

    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private int precioUnitario;

    private Compania compania;

    public String getArticulo() { return articulo; }
    public void setArticulo(String articulo) { this.articulo = articulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public int getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(int precioUnitario) { this.precioUnitario = precioUnitario; }

    public Compania getCompania() { return compania; }
    public void setCompania(Compania compania) { this.compania = compania; }
}
