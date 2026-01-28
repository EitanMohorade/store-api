package com.store.api.service;

import org.springframework.stereotype.Service;

import java.util.List;
import com.store.api.repository.ProductoRepository;
import com.store.api.entity.Producto;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.StockInsufficientException;
import com.store.api.exception.ValidationException;
import com.store.api.dto.producto.ProductoCreateDTO;
import com.store.api.dto.producto.ProductoUpdateDTO;
import com.store.api.dto.producto.ProductoResponseDTO;

/**
 * Servicio de negocio para la entidad Producto.
 * 
 * Proporciona operaciones de lógica de negocio para la gestión de productos,
 * incluyendo validación, búsqueda, filtrado y manipulación de stock.
 * Utiliza DTOs para la creación, actualización y respuestas de datos.
 * 
 */
@Service
public class ProductoService {
    
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un nuevo producto después de validarlo.
     * 
     * @param dto ProductoCreateDTO con los datos del producto a crear
     * @return ProductoResponseDTO el producto creado con ID generado
     * @throws ValidationException si el producto no cumple validaciones
     * @throws DuplicateResourceException si el artículo ya existe
     */
    public ProductoResponseDTO create(ProductoCreateDTO dto) {
        validate(dto);
        
        Producto producto = new Producto();
        producto.setArticulo(dto.getArticulo());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setCompania(dto.getCompania());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        
        Producto saved = productoRepository.save(producto);
        return toResponseDTO(saved);
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto
     * @return ProductoResponseDTO del producto encontrado
     * @throws ResourceNotFoundException si el producto no existe
     */
    public ProductoResponseDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return toResponseDTO(producto);
    }

    /**
     * Obtiene todos los productos disponibles.
     * 
     * @return Lista de ProductoResponseDTO de todos los productos
     */
    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Actualiza un producto existente.
     * 
     * @param id ID del producto a actualizar
     * @param dto ProductoUpdateDTO con los valores actualizados
     * @return ProductoResponseDTO del producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     * @throws ValidationException si los datos no cumplen validaciones
     */
    public ProductoResponseDTO update(Long id, ProductoUpdateDTO dto) {
        Producto existing = productoRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        validate(dto);

        existing.setArticulo(dto.getArticulo());
        existing.setDescripcion(dto.getDescripcion());
        existing.setCategoria(dto.getCategoria());
        existing.setCompania(dto.getCompania());
        existing.setImagenUrl(dto.getImagenUrl());
        existing.setPrecio(dto.getPrecio());
        existing.setStock(dto.getStock());
        existing.setPrecioUnitario(dto.getPrecioUnitario());

        Producto updated = productoRepository.save(existing);
        return toResponseDTO(updated);
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     * @throws ResourceNotFoundException si el producto no existe
     */
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        productoRepository.deleteById(id);
    }

    /**
     * Verifica si un producto existe por su ID.
     * 
     * @param id ID del producto
     * @return true si el producto existe, false en caso contrario
     */
    public boolean existById(Long id) {
        return productoRepository.existsById(id);
    }

    /**
     * Modifica el stock de un producto.
     * 
     * @param id ID del producto
     * @param cantidad Cantidad a sumar o restar del stock (puede ser negativa)
     * @throws ResourceNotFoundException si el producto no existe
     * @throws StockInsufficientException si el stock resultante es negativo
     */
    public void modifyStock(Long id, int cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new StockInsufficientException();
        }
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }

    /**
     * Encuentra todos los productos de una compañía específica.
     * 
     * @param companiaId ID de la compañía
     * @return Lista de ProductoResponseDTO de los productos de la compañía
     * @throws ValidationException si el ID de la compañía es nulo
     */
    public List<ProductoResponseDTO> findByCompaniaId(Long companiaId) {
        if (companiaId == null) {
            throw new ValidationException("El ID de la compañía no puede ser nulo");
        }
        return productoRepository.findByCompaniaId(companiaId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Encuentra todos los productos de una categoría específica.
     * 
     * @param categoriaId ID de la categoría
     * @return Lista de ProductoResponseDTO de los productos de la categoría
     * @throws ValidationException si el ID de la categoría es nulo
     */
    public List<ProductoResponseDTO> findByCategoriaId(Long categoriaId) {
        if (categoriaId == null) {
            throw new ValidationException("El ID de la categoría no puede ser nulo");
        }
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Encuentra productos que pertenecen a una compañía y categoría específicas.
     * 
     * @param companiaId ID de la compañía
     * @param categoriaId ID de la categoría
     * @return Lista de ProductoResponseDTO filtrados
     */
    public List<ProductoResponseDTO> findByCompaniaIdAndCategoriaId(Long companiaId, Long categoriaId) {
        return productoRepository.findByCompaniaIdAndCategoriaId(companiaId, categoriaId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Busca productos por coincidencia parcial en el número de artículo.
     * 
     * @param articulo Término de búsqueda (insensible a mayúsculas/minúsculas)
     * @return Lista de ProductoResponseDTO que coinciden con el término
     * @throws ValidationException si el término de búsqueda es nulo o vacío
     */
    public List<ProductoResponseDTO> findByArticuloContaining(String articulo) {
        if (articulo == null || articulo.isBlank()) {
            throw new ValidationException("El término de artículo no puede estar vacío");
        }
        return productoRepository.findByArticuloContainingIgnoreCase(articulo).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Encuentra productos con al menos la cantidad de stock especificada.
     * 
     * @param stock Cantidad mínima de stock
     * @return Lista de ProductoResponseDTO con stock >= cantidad especificada
     * @throws ValidationException si el stock es negativo
     */
    public List<ProductoResponseDTO> findByStock(int stock) {
        if (stock < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        return productoRepository.findByStock(stock).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Encuentra productos dentro de un rango de precios.
     * 
     * @param minPrecio Precio mínimo (inclusive)
     * @param maxPrecio Precio máximo (inclusive)
     * @return Lista de ProductoResponseDTO dentro del rango de precios
     * @throws ValidationException si los precios son negativos o el rango es inválido
     */
    public List<ProductoResponseDTO> findByPrecioRange(int minPrecio, int maxPrecio) {
        if (minPrecio < 0 || maxPrecio < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }
        if (minPrecio > maxPrecio) {
            throw new ValidationException("El rango de precios es inválido (min > max)");
        }
        return productoRepository.findByPrecioBetween(minPrecio, maxPrecio).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Obtiene el número total de productos disponibles.
     * 
     * @return Cantidad total de productos
     */
    public long countTotal() {
        return productoRepository.count();
    }

    /**
     * Obtiene el stock total de todos los productos.
     * 
     * @return Suma total del stock de todos los productos
     */
    public int getTotalStock() {
        return productoRepository.getTotalStock();
    }

    /**
     * Obtiene los productos agotados (stock = 0).
     * 
     * @return Lista de ProductoResponseDTO sin stock
     */
    public List<ProductoResponseDTO> findOutOfStockProducts() {
        return productoRepository.findByStock(0).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Convierte una entidad Producto a ProductoResponseDTO para no exponer atributos internos.
     * 
     * @param producto Entidad Producto
     * @return ProductoResponseDTO
     */
    private ProductoResponseDTO toResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getArticulo(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.getImagenUrl(),
                producto.getCompania()
        );
    }

    /**
     * Valida los datos de creación de un producto.
     * 
     * @param dto ProductoCreateDTO con los datos a validar
     * @throws ValidationException si alguna validación falla
     * @throws DuplicateResourceException si el artículo ya existe
     */
    private void validate(ProductoCreateDTO dto) {
        if (dto.getPrecio() < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }
        if (dto.getStock() < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (dto.getArticulo() == null || dto.getArticulo().isBlank()) {
            throw new ValidationException("El artículo no puede estar vacío");
        }
        if (productoRepository.existsByArticulo(dto.getArticulo())) {
            throw new DuplicateResourceException("El artículo ya existe");
        }
    }

    /**
     * Valida los datos de actualización de un producto.
     * 
     * @param dto ProductoUpdateDTO con los datos a validar
     * @throws ValidationException si alguna validación falla
     */
    private void validate(ProductoUpdateDTO dto) {
        if (dto.getPrecio() < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }
        if (dto.getStock() < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (dto.getArticulo() == null || dto.getArticulo().isBlank()) {
            throw new ValidationException("El artículo no puede estar vacío");
        }
    }

}
