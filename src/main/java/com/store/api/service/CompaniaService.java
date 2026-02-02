package com.store.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.api.dto.compania.CompaniaCreateDTO;
import com.store.api.dto.compania.CompaniaResponseDTO;
import com.store.api.dto.compania.CompaniaUpdateDTO;
import com.store.api.entity.Compania;
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

    private final CompaniaRepository companiaRepository;

    public CompaniaService(CompaniaRepository companiaRepository) {
        this.companiaRepository = companiaRepository;
    }

    /**
     * Crea una nueva compañía después de validarla.
     * 
     * @param dto CompaniaCreateDTO con datos de la compañía
     * @return CompaniaResponseDTO creada con ID generado
     * @throws ValidationException si la compañía no cumple validaciones
     */
    public CompaniaResponseDTO create(CompaniaCreateDTO dto) {
        Compania compania = toEntity(dto);
        validate(compania);
        Compania guardada = companiaRepository.save(compania);
        return toResponseDTO(guardada);
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
     * @param dto CompaniaUpdateDTO con datos actualizados
     * @return CompaniaResponseDTO actualizada
     * @throws ResourceNotFoundException si la compañía no existe
     * @throws ValidationException si la compañía no cumple validaciones
     */
    public CompaniaResponseDTO update(Long id, CompaniaUpdateDTO dto) {
        Compania existente = getEntityById(id);

        existente.setNombre(dto.getNombre());
        validate(existente);

        Compania guardada = companiaRepository.save(existente);
        return toResponseDTO(guardada);
    }

    /**
     * Obtiene una compañía por su ID.
     * 
     * @param id ID de la compañía
     * @return CompaniaResponseDTO encontrada
     * @throws ResourceNotFoundException si la compañía no existe
     */
    public CompaniaResponseDTO findById(Long id) {
        return toResponseDTO(getEntityById(id));
    }

    /**
     * Obtiene todas las compañías.
     * 
     * @return Lista de todas las compañías
     */
    public List<CompaniaResponseDTO> findAll() {
        return companiaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
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
     * @return CompaniaResponseDTO encontrada
     * @throws ResourceNotFoundException si la compañía no existe
     */
    public CompaniaResponseDTO findByNombre(String nombre) {
        Compania compania = companiaRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new ResourceNotFoundException());
        return toResponseDTO(compania);
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

    private Compania getEntityById(Long id) {
        return companiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    private Compania toEntity(CompaniaCreateDTO dto) {
        Compania compania = new Compania();
        compania.setNombre(dto.getNombre());
        return compania;
    }

    private CompaniaResponseDTO toResponseDTO(Compania compania) {
        CompaniaResponseDTO dto = new CompaniaResponseDTO();
        dto.setId(compania.getId());
        dto.setNombre(compania.getNombre());
        return dto;
    }
}
