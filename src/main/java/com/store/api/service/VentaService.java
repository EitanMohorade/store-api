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
     * Crea una nueva venta después de validarla.
     * 
     * @param venta Venta a crear
     * @return Venta creada con ID generado
     * @throws ValidationException si la venta no cumple validaciones
     */
    public Venta create(Venta venta) {
        validate(venta);
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
    private List<Venta> ventasEntre(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin);
    }

    /** Valida los datos de una venta.
     * 
     * @param venta Venta a validar
     * @throws ValidationException si los datos no son válidos
     */
    private void validate(Venta venta) {
        if (venta.getProducto() == null) {
            throw new ValidationException("El producto no puede ser nulo");
        }
        if (venta.getCantidad() == null || venta.getCantidad() <= 0) {
            throw new ValidationException("La cantidad debe ser mayor a cero");
        }
        if(ventaRepository.existsById(venta.getId())) {
            throw new DuplicateResourceException("Ya existe una venta con el mismo ID");
        }
    }
}
