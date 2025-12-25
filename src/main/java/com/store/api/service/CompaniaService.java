package com.store.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.entity.Compania;
import com.store.api.exception.InvalidStateException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.CompaniaRepository;

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
     * @throws ValidationException si la compañía no cumple validaciones
     */
    public Compania create(Compania compania) {
        validate(compania);
        return companiaRepository.save(compania);
    }

    /**
     * Elimina una compañía por su ID.
     * 
     * @param id ID de la compañía a eliminar
     * @throws ResourceNotFoundException si la compañía no existe
     */
    public void delete(Long id) {
        if (!companiaRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        companiaRepository.deleteById(id);
    }

    /**
     * Actualiza una compañía existente.
     * 
     * @param id ID de la compañía a actualizar
     * @param compania Compañía con datos actualizados
     * @return Compañía actualizada
     * @throws ResourceNotFoundException si la compañía no existe
     * @throws ValidationException si la compañía no cumple validaciones
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
     * @throws ResourceNotFoundException si la compañía no existe
     */
    public Compania findById(Long id) {
        return companiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
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
     * @throws ResourceNotFoundException si la compañía no existe
     */
    public Compania findByNombre(String nombre) {
        return companiaRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new ResourceNotFoundException());
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
     * @throws ValidationException si los datos no son válidos
     */
    private void validate(Compania compania) {
        if (compania.getNombre() == null || compania.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre de la compañía es obligatorio");
        }
        if (compania.getNombre().length() > 100) {
            throw new ValidationException("El nombre de la compañía no puede exceder 100 caracteres");
        }
        if(companiaRepository.existsByNombre(compania.getNombre())) {
            throw new ValidationException("Ya existe una compañía con el mismo nombre");
        }
    }
}
