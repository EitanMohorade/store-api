package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa la configuración global de la tienda.
 *
 * Es un singleton: siempre existe un único registro con ID = 1.
 * Se inicializa en el arranque con valores por defecto si no existe.
 */
@Entity
public class ConfiguracionTienda {

    /** Siempre será 1 (singleton). */
    @Id
    private Long id = 1L;

    /** Nombre público de la tienda. */
    private String nombre;

    /** Dirección física del local. */
    private String direccion;

    /** Eslogan o tagline de la tienda. */
    private String tagline;

    /** Descripción general de la tienda. */
    @Column(length = 1000)
    private String descripcion;

    public ConfiguracionTienda() {}

    public ConfiguracionTienda(String nombre, String direccion, String tagline, String descripcion) {
        this.id = 1L;
        this.nombre = nombre;
        this.direccion = direccion;
        this.tagline = tagline;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
