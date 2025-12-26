package com.store.api.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.entity.Venta;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.VentaRepository;

import lombok.val;

@Service
public class VentaService {
    @Autowired

    private VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    /**
     * Crea una nueva venta después de validarla y restar el stock del producto.
     * 
     * @param venta Venta a crear
     * @return Venta creada con ID generado
     * @throws ValidationException si la venta no cumple validaciones
     */
    public Venta create(Venta venta) {
        validate(venta);
        venta.getProducto().setStock(
            venta.getProducto().getStock() - venta.getCantidad()
        );
        return ventaRepository.save(venta);
    }

    /**
     * Obtiene todas las ventas registradas.
     * 
     * @return Lista de todas las ventas
     */
    public List<Venta> ventasDeHoy() {
        LocalDate hoy = LocalDate.now();
        return ventasEntre(
            hoy.atStartOfDay(),
            hoy.atTime(LocalTime.MAX)
        );
    }

    /**
     * Obtiene todas las ventas realizadas en la semana actual (de lunes a domingo).
     * 
     * @return Lista de ventas de la semana actual
     */
    public List<Venta> ventasDeLaSemana() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate finSemana = inicioSemana.plusDays(6);

        return ventasEntre(
            inicioSemana.atStartOfDay(),
            finSemana.atTime(LocalTime.MAX)
        );
    }

    /**
     * Obtiene todas las ventas realizadas en el mes actual.
     * 
     * @return Lista de ventas del mes actual
     */
    public List<Venta> ventasDelMes() {
        YearMonth mes = YearMonth.now();

        return ventasEntre(
            mes.atDay(1).atStartOfDay(),
            mes.atEndOfMonth().atTime(LocalTime.MAX)
        );
    }

    /**
     * Obtiene todas las ventas realizadas en un rango de fechas específico.
     * 
     * @param inicio Fecha y hora de inicio del rango (inclusive)
     * @param fin Fecha y hora de fin del rango (inclusive)
     * @return Lista de ventas dentro del rango especificado
     */
    List<Venta> ventasEntre(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin);
    }

    /** Obtiene el precio unitario de una venta.
     * 
     * @param venta Venta de la cual obtener el precio unitario
     * @return Integer con el precio unitario del producto vendido
     */
    Integer getPrecioUnitario(Venta venta) {
        return venta.getProducto().getPrecioUnitario();
    }

    /** Obtiene el precio total de una venta.
     * 
     * @param venta Venta de la cual obtener el precio total
     * @return Integer con el precio total del producto vendido
     */
    Integer getPrecio(Venta venta) {
        return venta.getProducto().getPrecio();
    }

    /** Obtiene el total del precio unitario de una venta.
     * 
     * @param venta Venta de la cual obtener el total del precio unitario
     * @return Integer con el total del precio unitario del producto vendido
     */
    Integer getTotalPrecioUnitario(Venta venta) {
        return venta.getCantidad() * getPrecioUnitario(venta);
    }


    /** Obtiene el total del precio de una venta.
     * 
     * @param venta Venta de la cual obtener el total del precio
     * @return Integer con el total del precio del producto vendido
     */
    Integer getTotalPrecioCliente(Venta venta) {
        return venta.getCantidad() * getPrecio(venta);
    }

    /** Valida los datos de una venta.
     * 
     * @param venta Venta a validar
     * @throws ValidationException si el producto es nulo o la cantidad es inválida o vendieron mas de lo que hay en stock
     * @throws DuplicateResourceException si ya existe una venta con el mismo ID
     */
    private void validate(Venta venta) {
        if (venta.getProducto() == null) {
            throw new ValidationException("El producto de la venta no puede ser nulo");
        }
        if (venta.getCantidad() > venta.getProducto().getStock()) {
            throw new ValidationException("No hay suficiente stock para completar la venta");
        }
        if (venta.getCantidad() == null || venta.getCantidad() <= 0) {
            throw new ValidationException("La cantidad debe ser mayor a cero");
        }
        if(ventaRepository.existsById(venta.getId())) {
            throw new DuplicateResourceException("Ya existe una venta con el mismo ID");
        }
    }
}
