package com.store.api.controller;

import org.springframework.web.bind.annotation.*;

import com.store.api.dto.venta.VentaCreateDTO;
import com.store.api.dto.venta.VentaUpdateDTO;
import com.store.api.dto.venta.VentaResponseDTO;
import com.store.api.service.VentaService;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    /**
     * Obtiene todas las ventas registradas.
     * 
     * @return Lista de todas las ventas
     */
    @GetMapping
    public List<VentaResponseDTO> findAll() {
        return ventaService.findAll();
    }

    /**
     * Obtiene una venta por su ID.
     * 
     * @param id ID de la venta
     * @return VentaResponseDTO de la venta encontrada
     */
    @GetMapping("/{id}")
    public VentaResponseDTO findById(@PathVariable Long id) {
        return ventaService.findById(id);
    }

    /**
     * Crea una nueva venta.
     * 
     * @param dto VentaCreateDTO con los datos de la venta
     * @return VentaResponseDTO de la venta creada
     */
    @PostMapping
    public VentaResponseDTO create(@RequestBody VentaCreateDTO dto) {
        return ventaService.create(dto);
    }

    /**
     * Actualiza una venta existente.
     * 
     * @param id ID de la venta a actualizar
     * @param dto VentaUpdateDTO con los datos actualizados
     * @return VentaResponseDTO de la venta actualizada
     */
    @PutMapping("/{id}")
    public VentaResponseDTO update(@PathVariable Long id, @RequestBody VentaUpdateDTO dto) {
        return ventaService.update(id, dto);
    }

    /**
     * Elimina una venta por su ID.
     * 
     * @param id ID de la venta a eliminar
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ventaService.delete(id);
    }

    /**
     * Obtiene las ventas del día actual.
     * 
     * @return Lista de ventas de hoy
     */
    @GetMapping("/hoy")
    public List<VentaResponseDTO> ventasDeHoy() {
        return ventaService.ventasDeHoy();
    }

    /**
     * Obtiene las ventas de la semana actual.
     * 
     * @return Lista de ventas de la semana
     */
    @GetMapping("/semana")
    public List<VentaResponseDTO> ventasDeLaSemana() {
        return ventaService.ventasDeLaSemana();
    }

    /**
     * Obtiene las ventas del mes actual.
     * 
     * @return Lista de ventas del mes
     */
    @GetMapping("/mes")
    public List<VentaResponseDTO> ventasDelMes() {
        return ventaService.ventasDelMes();
    }
}
