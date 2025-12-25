package com.store.api.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa un usuario administrador del sistema.
 * 
 * Los administradores tienen acceso para modificar, eliminar y gestionar todo en la aplicación.
 * 
 * @param id Identificador único del administrador
 * @param nombre Nombre de usuario del administrador
 * @param password Contraseña encriptada del administrador
 */
@Entity
public class Admin {

    /** Identificador único del administrador. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Nombre de usuario del administrador. */
    @Column(nullable = false, unique = true)
    private String nombre;
    
    /** Contraseña encriptada del administrador. */
    @Column(name = "password_hash", nullable = false)
    private String password;

    public Admin() {}

    public Admin(Long id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
