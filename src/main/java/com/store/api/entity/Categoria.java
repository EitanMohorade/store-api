package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa una categoría de productos.
 * 
 * Las categorías agrupan productos relacionados para facilitar la organización
 * y búsqueda en el sistema de inventario.
 * 
 * @param id Identificador único de la categoría
 * @param nombre Nombre de la categoría
 * @param descripcion Descripción de la categoría
 * 
 */
@Entity
public class Categoria {

    /** Identificador único de la categoría. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Nombre de la categoría. */
    private String nombre;
    
    /** Descripción de la categoría. */
    private String descripcion;

    public Categoria() {}

    public Categoria(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
