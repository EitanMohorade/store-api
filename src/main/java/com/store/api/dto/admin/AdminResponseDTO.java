package com.store.api.dto.admin;

/**
 * DTO para la respuesta de un administrador.
*/
public class AdminResponseDTO {
    private Long id;
    private String nombre;


    public AdminResponseDTO() {}

    /**
     * Constructor con parámetros.
     * 
     * @param id ID del administrador
     * @param nombre Nombre del administrador
    */
    public AdminResponseDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID del administrador.
     * @return id
    */
    public Long getId() {
        return id;
    }

    /**
     * Obtiene el nombre del administrador.
     * @return nombre
    */
    public String getNombre() {
        return nombre;
    }
}
