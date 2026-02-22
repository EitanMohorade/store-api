package com.store.api.dto.compania;

import jakarta.validation.constraints.*;

public class CompaniaCreateDTO {
    private Long id;
    @NotBlank(message = "El nombre de la compañía no puede estar vacío")
    private String nombre;

    public void setId(Long id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
}
