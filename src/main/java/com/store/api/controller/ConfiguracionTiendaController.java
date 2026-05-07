package com.store.api.controller;

import com.store.api.dto.configuracionTienda.ConfiguracionTiendaDTO;
import com.store.api.service.ConfiguracionTiendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para la configuración global de la tienda.
 *
 * GET /api/configuracion  → público (como productos y categorías)
 * PUT /api/configuracion  → solo ADMIN
 */
@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionTiendaController {

    private final ConfiguracionTiendaService service;

    public ConfiguracionTiendaController(ConfiguracionTiendaService service) {
        this.service = service;
    }

    /**
     * Obtiene la configuración actual de la tienda.
     * Accesible públicamente (sin autenticación según config de seguridad actual).
     */
    @GetMapping
    public ResponseEntity<ConfiguracionTiendaDTO> get() {
        return ResponseEntity.ok(service.get());
    }

    /**
     * Actualiza la configuración de la tienda.
     * Requiere rol ADMIN.
     */
    @PutMapping
    public ResponseEntity<ConfiguracionTiendaDTO> update(@Valid @RequestBody ConfiguracionTiendaDTO dto) {
        return ResponseEntity.ok(service.update(dto));
    }
}
