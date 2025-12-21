package com.store.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.api.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Verifica si existe un producto con el artículo especificado.
     * 
     * @param articulo Artículo del producto
     * @return true si existe un producto con el artículo, false en caso contrario
     */
    boolean existsByArticulo(String articulo);

    /**
     * Encuentra productos cuyo precio esté entre los valores especificados.
     * 
     * @param i Precio mínimo
     * @param j Precio máximo
     * @return Lista de productos dentro del rango de precios
     */
    List<Producto> findByPrecioBetween(int i, int j);

    /**
     * Encuentra productos con el stock especificado.
     * 
     * @param stock Cantidad de stock
     * @return Lista de productos con el stock especificado
     */
    List<Producto> findByStock(int stock);

    /**
     * Encuentra productos cuyo artículo contenga la cadena especificada (sin distinguir mayúsculas/minúsculas).
     * 
     * @param articulo Cadena a buscar en el artículo
     * @return Lista de productos cuyo artículo contiene la cadena especificada
     */
    List<Producto> findByArticuloContainingIgnoreCase(String articulo);

    /**
     * Encuentra productos por el ID de su categoría.
     * 
     * @param categoriaId ID de la categoría
     * @return Lista de productos pertenecientes a la categoría especificada
     */
    List<Producto> findByCategoriaId(Long categoriaId);


    /**
     * Encuentra productos por el ID de su compañía.
     * 
     * @param companiaId ID de la compañía
     * @return Lista de productos pertenecientes a la compañía especificada
     */
    List<Producto> findByCompaniaId(Long companiaId);

    /**
     * Encuentra productos que pertenecen a una compañía y categoría específicas.
     * 
     * @param companiaId ID de la compañía
     * @param categoriaId ID de la categoría
     * @return Lista de productos filtrados
     */
    List<Producto> findByCompaniaIdAndCategoriaId(Long companiaId, Long categoriaId);

    /**
     * Obtiene el stock total de todos los productos.
     * 
     * @return Stock total
     */
    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Producto p")
    int getTotalStock();

}

