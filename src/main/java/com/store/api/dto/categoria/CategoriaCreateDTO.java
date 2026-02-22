package com.store.api.dto.categoria;

import jakarta.validation.constraints.*;

public class CategoriaCreateDTO {
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombre;
    
    private String descripcion;

    public void setId(Long id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
