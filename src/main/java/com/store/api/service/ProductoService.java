package com.store.api.service;

import org.springframework.stereotype.Service;

import java.util.List;
import com.store.api.repository.ProductoRepository;
import com.store.api.entity.Producto;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.StockInsufficientException;
import com.store.api.exception.ValidationException;

/**
 * Servicio de negocio para la entidad Producto.
 * 
 * Proporciona operaciones de lógica de negocio para la gestión de productos,
 * incluyendo validación, búsqueda, filtrado y manipulación de stock.
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
     * @param producto Producto a crear
     * @return Producto creado con ID generado
     * @throws ValidationException si el producto no cumple validaciones
     * @throws DuplicateResourceException si el artículo ya existe
     */
    public Producto create(Producto Producto) {
        validate(Producto);
        return productoRepository.save(Producto);
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto
     * @return Producto encontrado
     * @throws ResourceNotFoundException si el producto no existe
     */
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Obtiene todos los productos disponibles.
     * 
     * @return Lista de todos los productos
     */
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    /**
     * Actualiza un producto existente.
     * 
     * @param id ID del producto a actualizar
     * @param data Datos del producto con los valores actualizados
     * @return Producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     * @throws ValidationException si los datos no cumplen validaciones
     */
    public Producto update(Long id, Producto data) {
        
        Producto existing = findById(id);

        existing.setArticulo(data.getArticulo());
        existing.setDescripcion(data.getDescripcion());
        existing.setCategoria(data.getCategoria());
        existing.setCompania(data.getCompania());
        existing.setImagenUrl(data.getImagenUrl());
        existing.setPrecio(data.getPrecio());
        existing.setStock(data.getStock());
        existing.setPrecioUnitario(data.getPrecioUnitario());

        validate(existing);

        return productoRepository.save(existing);
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
     * @throws StockInsufficientException si el stock resultante es negativo
     */
    public void modifyStock(Long id, int cantidad) {
        Producto producto = findById(id);
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
     * @return Lista de productos de la compañía
     * @throws ValidationException si el ID de la compañía es nulo
     */
    public List<Producto> findByCompaniaId(Long companiaId) {
        if (companiaId == null) {
            throw new ValidationException("El ID de la compañía no puede ser nulo");
        }
        return productoRepository.findByCompaniaId(companiaId);
    }

    /**
     * Encuentra todos los productos de una categoría específica.
     * 
     * @param categoriaId ID de la categoría
     * @return Lista de productos de la categoría
     * @throws ValidationException si el ID de la categoría es nulo
     */
    public List<Producto> findByCategoriaId(Long categoriaId) {
        if (categoriaId == null) {
            throw new ValidationException("El ID de la categoría no puede ser nulo");
        }
        return productoRepository.findByCategoriaId(categoriaId);
    }

    /**
     * Encuentra productos que pertenecen a una compañía y categoría específicas.
     * 
     * @param companiaId ID de la compañía
     * @param categoriaId ID de la categoría
     * @return Lista de productos filtrados
     */
    public List<Producto> findByCompaniaIdAndCategoriaId(Long companiaId, Long categoriaId) {
        return productoRepository.findByCompaniaIdAndCategoriaId(companiaId, categoriaId);
    }

    /**
     * Busca productos por coincidencia parcial en el número de artículo.
     * 
     * @param articulo Término de búsqueda (insensible a mayúsculas/minúsculas)
     * @return Lista de productos que coinciden con el término
     * @throws ValidationException si el término de búsqueda es nulo o vacío
     * */
    public List<Producto> findByArticuloContaining(String articulo) {
        if (articulo == null || articulo.isBlank()) {
            throw new ValidationException("El término de artículo no puede estar vacío");
        }
        return productoRepository.findByArticuloContainingIgnoreCase(articulo);
    }

    /**
     * Encuentra productos con al menos la cantidad de stock especificada.
     * 
     * @param stock Cantidad mínima de stock
     * @return Lista de productos con stock >= cantidad especificada
     * @throws ValidationException si el stock es negativo
     */
    public List<Producto> findByStock(int stock) {
        if (stock < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        return productoRepository.findByStock(stock);
    }

    /**
     * Encuentra productos dentro de un rango de precios.
     * 
     * @param minPrecio Precio mínimo (inclusive)
     * @param maxPrecio Precio máximo (inclusive)
     * @return Lista de productos dentro del rango de precios
     * @throws ValidationException si los precios son negativos o el rango es inválido
     */
    public List<Producto> findByPrecioRange(int minPrecio, int maxPrecio) {
        if (minPrecio < 0 || maxPrecio < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }
        if (minPrecio > maxPrecio) {
            throw new ValidationException("El rango de precios es inválido (min > max)");
        }
        return productoRepository.findByPrecioBetween(minPrecio, maxPrecio);
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
     * @return Lista de productos sin stock
     */
    public List<Producto> findOutOfStockProducts() {
        return productoRepository.findByStock(0);
    }

    /**
     * Valida los atributos de un producto.
     * 
     * @param p Producto a validar
     * @throws ValidationException si alguna validación falla
     * @throws DuplicateResourceException si el artículo ya existe
     */
    private void validate(Producto p) {
        if (p.getPrecio() < 0) {
            throw new ValidationException("El precio no puede ser negativo");
        }
        if (p.getStock() < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (p.getArticulo() == null || p.getArticulo().isBlank()) {
            throw new ValidationException("El artículo no puede estar vacío");
        }
        if (productoRepository.existsByArticulo(p.getArticulo())) {
            throw new DuplicateResourceException("El artículo ya existe");
        }
    }

}
