package com.store.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.api.entity.Venta;

/**
 * Repositorio JPA para la entidad Venta.
 * 
 * Proporciona operaciones CRUD (Create, Read, Update, Delete) para las ventas
 * en el sistema de inventario. Esta interfaz extiende JpaRepository, lo que permite
 * acceder a métodos heredados para:
 * 
 * - Guardar ventas: save()
 * - Recuperar ventas: findById(), findAll()
 * - Actualizar ventas: save() con una venta existente
 * - Eliminar ventas: delete(), deleteById()
 * - Contar ventas: count()
 * 
 * Las ventas representan transacciones de productos vendidos a clientes. Cada venta
 * está asociada a un producto y contiene información de cantidad, precio unitario
 * y fecha de realización.
 * 
 * 
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Encuentra todas las ventas realizadas en una fecha específica.
     * 
     * @param fecha Fecha de la venta (se comparará por día)
     * @return Lista de ventas del día especificado
     */
    List<Venta> findByFecha(LocalDateTime fecha);

    /**
     * Encuentra todas las ventas realizadas después de una fecha específica.
     * 
     * @param fecha Fecha límite (inclusive)
     * @return Lista de ventas posteriores a la fecha
     */
    List<Venta> findByFechaGreaterThanEqual(LocalDateTime fecha);

    /**
     * Encuentra todas las ventas realizadas antes de una fecha específica.
     * 
     * @param fecha Fecha límite (inclusive)
     * @return Lista de ventas anteriores a la fecha
     */
    List<Venta> findByFechaLessThanEqual(LocalDateTime fecha);

    /**
     * Encuentra todas las ventas dentro de un rango de fechas.
     * 
     * @param fechaInicio Fecha de inicio (inclusive)
     * @param fechaFin Fecha de fin (inclusive)
     * @return Lista de ventas dentro del rango
     */
    List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
