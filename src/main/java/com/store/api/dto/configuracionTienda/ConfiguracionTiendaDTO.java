package com.store.api.dto.configuracionTienda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para leer y actualizar la configuración de la tienda.
 * Se usa tanto para la respuesta (GET) como para la actualización (PUT).
 */
public class ConfiguracionTiendaDTO {

    @NotBlank(message = "El nombre de la tienda no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String direccion;

    @Size(max = 200, message = "El tagline no puede superar los 200 caracteres")
    private String tagline;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    public ConfiguracionTiendaDTO() {}

    public ConfiguracionTiendaDTO(String nombre, String direccion, String tagline, String descripcion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tagline = tagline;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
