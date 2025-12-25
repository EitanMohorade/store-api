package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa una compañía o empresa.
 * 
 * Las compañías son las que produjeron o distribuyen los productos en el sistema de inventario.
 * 
 * @param id Identificador único de la compañía
 * @param nombre Nombre de la compañía o empresa
 * 
 */
@Entity
public class Compania {

    /** Identificador único de la compañía. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Nombre de la compañía o empresa. */
    private String nombre;

    public Compania() {}

    public Compania(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

}
