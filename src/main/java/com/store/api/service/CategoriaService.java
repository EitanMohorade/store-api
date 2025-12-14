package com.store.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.entity.Categoria;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.CategoriaRepository;

import java.util.List;

/**
 * Servicio de negocio para la entidad Categoría.
 * 
 * Proporciona operaciones de lógica de negocio para la gestión de categorías,
 * incluyendo validación, búsqueda y manipulación de datos.
 * 
 */
@Service
public class CategoriaService {
    @Autowired

    private CategoriaRepository categoriaRepository;

    /**
     * Crea una nueva categoría después de validarla.
     * 
     * @param categoria Categoría a crear
     * @return Categoría creada con ID generado
     * @throws ValidationException si la categoría no cumple validaciones
     */
    public Categoria create(Categoria categoria) {
        validate(categoria);
        
        return categoriaRepository.save(categoria);
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
     * Encuentra una categoría por su ID.
     * 
     * @param id ID de la categoría a buscar
     * @return Categoría encontrada
     * @throws ResourceNotFoundException si la categoría no existe
     */
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    /**
     * Busca categorías cuyo nombre contenga el término especificado (insensible a mayúsculas/minúsculas).
     * 
     * @param nombre Término de búsqueda
     * @return Lista de categorías que coinciden con el término
     */
    public List<Categoria> findByNombreContaining(String nombre) {
        return categoriaRepository.findAll().stream()
                .filter(categoria -> categoria.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    /**
     * Recupera todas las categorías.
     * 
     * @return Lista de todas las categorías
     */
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    /**
     * Actualiza una categoría existente.
     * 
     * @param id ID de la categoría a actualizar
     * @param categoria Categoría con datos actualizados
     * @return Categoría actualizada
     * @throws ResourceNotFoundException si la categoría no existe
     * @throws ValidationException si la categoría no cumple validaciones
     */
    public Categoria update(Long id, Categoria categoria) {
        Categoria existente = findById(id);

        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        validate(existente);

        return categoriaRepository.save(existente);
    }

    /**
     * Actualiza una categoría existente.
     * 
     * @param id ID de la categoría a actualizar
     * @param categoria Categoría con datos actualizados
     * @return Categoría actualizada
     * @throws ResourceNotFoundException si la categoría no existe
     * @throws ValidationException si la categoría no cumple validaciones
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
}
