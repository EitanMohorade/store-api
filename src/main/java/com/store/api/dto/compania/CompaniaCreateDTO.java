package com.store.api.dto.compania;

public class CompaniaCreateDTO {
    private Long id;
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
