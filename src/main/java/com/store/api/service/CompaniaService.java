package com.store.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.entity.Compania;
import com.store.api.repository.CompaniaRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Servicio de negocio para la entidad Compañía.
 * 
 * Proporciona operaciones de lógica de negocio para la gestión de compañías,
 * incluyendo validación, búsqueda y manipulación de datos.
 * 
 */
@Service
public class CompaniaService {
    @Autowired

    private CompaniaRepository companiaRepository;

    /**
     * Crea una nueva compañía después de validarla.
     * 
     * @param compania Compañía a crear
     * @return Compañía creada con ID generado
     * @throws IllegalArgumentException si la compañía no cumple validaciones
     */
    public Compania create(Compania compania) {
        validate(compania);
        return companiaRepository.save(compania);
    }

    /**
     * Elimina una compañía por su ID.
     * 
     * @param id ID de la compañía a eliminar
     * @throws RuntimeException si la compañía no existe
     */
    public void delete(Long id) {
        if (!companiaRepository.existsById(id)) {
            throw new RuntimeException("Compañía no encontrada");
        }
        companiaRepository.deleteById(id);
    }

    /**
     * Actualiza una compañía existente.
     * 
     * @param id ID de la compañía a actualizar
     * @param compania Compañía con datos actualizados
     * @throws IllegalArgumentException si la compañía no cumple validaciones
     */
    public Compania update(Long id, Compania compania) {
        Compania existente = findById(id);

        existente.setNombre(compania.getNombre());
        validate(existente);

        return companiaRepository.save(existente);
    }

    /**
     * Obtiene una compañía por su ID.
     * 
     * @param id ID de la compañía
     * @return Compañía encontrada
     * @throws RuntimeException si la compañía no existe
     */
    public Compania findById(Long id) {
        return companiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compañía no encontrada"));
    }

    /**
     * Obtiene todas las compañías.
     * 
     * @return Lista de todas las compañías
     */
    public List<Compania> findAll() {
        return companiaRepository.findAll();
    }

    /**
     * Verifica si una compañía existe por su ID.
     * 
     * @param id ID de la compañía
     * @return true si la compañía existe, false en caso contrario
     */
    public boolean existsById(Long id) {
        return companiaRepository.existsById(id);
    }

    /**
     * Obtiene una compañía por su nombre.
     * 
     * @param nombre Nombre de la compañía
     * @return Compañía encontrada
     * @throws RuntimeException si la compañía no existe
     */
    public Compania findByNombre(String nombre) {
        return companiaRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Compañía no encontrada"));
    }
    
    /**
     * Cuenta el número total de compañías.
     * 
     * @return Número total de compañías
     */
    public Long count() {
        return companiaRepository.count();
    }


    /** Valida los datos de una compañía.
     * 
     * @param compania Compañía a validar
     * @throws IllegalArgumentException si la compañía no cumple validaciones
     */
    private void validate(Compania compania) {
        if (compania.getNombre() == null || compania.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la compañía no puede estar vacío");
        }
        if (compania.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre de la compañía no puede exceder 100 caracteres");
        }
    }
}
