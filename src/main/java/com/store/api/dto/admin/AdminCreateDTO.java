package com.store.api.dto.admin;

/**
 * DTO para la creación de un administrador.
*/
public class AdminCreateDTO {
    
    private String nombre;
    private String password;

    /**
     * Obtiene el nombre del administrador.
     * @return nombre
    */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del administrador.
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la contraseña del administrador.
     * @return password
    */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del administrador.
     * @param password
    */
    public void setPassword(String password) {
        this.password = password;
    }
}
