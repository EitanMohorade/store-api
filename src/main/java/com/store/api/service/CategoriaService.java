package com.store.api.service;

import org.springframework.stereotype.Service;

import com.store.api.dto.categoria.CategoriaCreateDTO;
import com.store.api.dto.categoria.CategoriaResponseDTO;
import com.store.api.dto.categoria.CategoriaUpdateDTO;
import com.store.api.entity.Categoria;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.CategoriaRepository;

import java.util.List;

/**
 * Servicio de negocio para la entidad Categoría.
 *
 * Opera con DTOs de entrada/salida y encapsula la validación,
 * la persistencia y el mapeo entre entidad y DTO.
 */
@Service
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Crea una nueva categoría desde el DTO de creación y devuelve el DTO de respuesta.
     *
     * @param dto CategoriaCreateDTO datos de la categoría a crear
     * @return categoría creada con ID generado
     * @throws ValidationException si no cumple validaciones
     */
    public CategoriaResponseDTO create(CategoriaCreateDTO dto) {
        Categoria categoria = toEntity(dto);
        validate(categoria);

        Categoria guardada = categoriaRepository.save(categoria);
        return toResponseDTO(guardada);
    }

    /**
     * Elimina una categoría por su ID.
     *
     * @param id ID de la categoría a eliminar
     * @throws ResourceNotFoundException si la categoría no existe
     */
    public void delete(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        categoriaRepository.deleteById(id);
    }

    /**
     * Busca una categoría por ID y devuelve el DTO de respuesta.
     *
     * @param id ID de la categoría a buscar
     * @return categoría encontrada
     * @throws ResourceNotFoundException si la categoría no existe
     */
    public CategoriaResponseDTO findById(Long id) {
        return toResponseDTO(getEntityById(id));
    }

    /**
     * Recupera todas las categorías y devuelve sus DTOs de respuesta.
     *
     * @return lista de categorías
     */
    public List<CategoriaResponseDTO> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Actualiza una categoría existente usando el DTO de actualización.
     *
     * @param id ID de la categoría a actualizar
     * @param dto CategoriaUpdateDTO datos a actualizar
     * @return categoría actualizada
     * @throws ResourceNotFoundException si la categoría no existe
     * @throws ValidationException si no cumple validaciones
     */
    public CategoriaResponseDTO update(Long id, CategoriaUpdateDTO dto) {
        Categoria existente = getEntityById(id);

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        validate(existente);

        Categoria guardada = categoriaRepository.save(existente);
        return toResponseDTO(guardada);
    }

    /**
     * Valida una categoría antes de su creación o actualización.
     *
     * @param categoria Categoria entidad a validar
     * @throws ValidationException si el nombre es nulo, vacío o excede 100 caracteres
     * @throws DuplicateResourceException si ya existe una categoría con el mismo nombre
     */
    private void validate(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new ValidationException("El nombre de la categoría no puede estar vacío");
        }
        if (categoria.getNombre().length() > 100) {
            throw new ValidationException("El nombre de la categoría no puede exceder 100 caracteres");
        }
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new DuplicateResourceException("Ya existe una categoría con el mismo nombre");
        }
    }

    private Categoria getEntityById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    private Categoria toEntity(CategoriaCreateDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoria;
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
