package com.store.api.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.store.api.entity.Venta;
import com.store.api.dto.venta.VentaCreateDTO;
import com.store.api.dto.venta.VentaUpdateDTO;
import com.store.api.dto.venta.VentaResponseDTO;
import com.store.api.exception.DuplicateResourceException;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.VentaRepository;


@Service
public class VentaService {
    
    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    /**
     * Crea una nueva venta después de validarla y restar el stock del producto.
     * 
     * @param dto VentaCreateDTO con los datos de la venta a crear
     * @return VentaResponseDTO creada con ID generado
     * @throws ValidationException si la venta no cumple validaciones
     */
    public VentaResponseDTO create(VentaCreateDTO dto) {
        Venta venta = toEntity(dto);
        
        validate(venta, true);
        venta.getProducto().setStock(
            venta.getProducto().getStock() - venta.getCantidad()
        );
        
        Venta saved = ventaRepository.save(venta);
        return toResponseDTO(saved);
    }

    /**
     * Obtiene todas las ventas registradas.
     * 
     * @return Lista de todas las ventas con formato DTO
     */
    public List<VentaResponseDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Obtiene una venta por su ID.
     * 
     * @param id ID de la venta
     * @return VentaResponseDTO del venta encontrada
     * @throws ResourceNotFoundException si la venta no existe
     */
    public VentaResponseDTO findById(Long id) {
        Venta venta = getEntityById(id);
        return toResponseDTO(venta);
    }

    /**
     * Actualiza una venta existente.
     * 
     * @param id ID de la venta a actualizar
     * @param dto VentaUpdateDTO con los valores actualizados
     * @return VentaResponseDTO de la venta actualizada
     * @throws ResourceNotFoundException si la venta no existe
     * @throws ValidationException si los datos no cumplen validaciones
     */
    public VentaResponseDTO update(Long id, VentaUpdateDTO dto) {
        Venta existing = getEntityById(id);
        applyUpdateDTO(existing, dto);
        validate(existing, false);

        Venta updated = ventaRepository.save(existing);
        return toResponseDTO(updated);
    }

    /**
     * Elimina una venta por su ID.
     * 
     * @param id ID de la venta a eliminar
     * @throws ResourceNotFoundException si la venta no existe
     */
    public void delete(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe una venta con el ID: " + id);
        }
        ventaRepository.deleteById(id);
    }

    /**
     * Obtiene todas las ventas registradas.
     * 
     * @return Lista de todas las ventas
     */
    public List<VentaResponseDTO> ventasDeHoy() {
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
    public List<VentaResponseDTO> ventasDeLaSemana() {
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
    public List<VentaResponseDTO> ventasDelMes() {
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
    List<VentaResponseDTO> ventasEntre(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /** Obtiene el precio unitario de una venta.
     * 
     * @param venta Venta de la cual obtener el precio unitario
     * @return Integer con el precio unitario del producto vendido
     */
    Integer getPrecioUnitario(VentaResponseDTO venta) {
        return venta.getPrecioUnitario();
    }

    /** Obtiene el precio total de una venta.
     * 
     * @param venta Venta de la cual obtener el precio total
     * @return Integer con el precio total del producto vendido
     */
    Integer getPrecio(VentaResponseDTO venta) {
        return venta.getPrecio();
    }

    /** Obtiene el total del precio unitario de una venta.
     * 
     * @param venta Venta de la cual obtener el total del precio unitario
     * @return Integer con el total del precio unitario del producto vendido
     */
    Integer getTotalPrecioUnitario(VentaResponseDTO venta) {
        return venta.getTotalPrecioUnitario();
    }


    /** Obtiene el total del precio de una venta.
     * 
     * @param venta Venta de la cual obtener el total del precio
     * @return Integer con el total del precio del producto vendido
     */
    Integer getTotalPrecioCliente(VentaResponseDTO venta) {
        return venta.getTotalPrecio();
    }

    /** Valida los datos de una venta.
     * 
     * @param venta Venta a validar
     * @throws ValidationException si el producto es nulo o la cantidad es inválida o vendieron mas de lo que hay en stock
     * @throws DuplicateResourceException si ya existe una venta con el mismo ID
     */
    private void validate(Venta venta, boolean isCreate) {
        if (venta.getProducto() == null) {
            throw new ValidationException("El producto de la venta no puede ser nulo");
        }
        if (venta.getCantidad() == null || venta.getCantidad() <= 0) {
            throw new ValidationException("La cantidad debe ser mayor a cero");
        }
        if (venta.getCantidad() > venta.getProducto().getStock()) {
            throw new ValidationException("No hay suficiente stock para completar la venta");
        }
        if (isCreate && venta.getId() != null && ventaRepository.existsById(venta.getId())) {
            throw new DuplicateResourceException("Ya existe una venta con el mismo ID");
        }
    }

    private Venta getEntityById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private Venta toEntity(VentaCreateDTO dto) {
        Venta venta = new Venta();
        venta.setId(dto.getId());
        venta.setProducto(dto.getProducto());
        venta.setCantidad(dto.getCantidad());
        venta.setFecha(dto.getFecha());
        return venta;
    }

    private void applyUpdateDTO(Venta venta, VentaUpdateDTO dto) {
        venta.setProducto(dto.getProducto());
        venta.setCantidad(dto.getCantidad());
        venta.setFecha(dto.getFecha());
    }

    /**
     * Convierte una entidad Venta a VentaResponseDTO para no exponer atributos internos.
     * 
     * @param venta Entidad Venta
     * @return VentaResponseDTO
     */
    private VentaResponseDTO toResponseDTO(Venta venta) {
        return new VentaResponseDTO(
            venta.getId(),
            venta.getProducto(),
            venta.getCantidad(),
            venta.getFecha()
        );
    }
}
