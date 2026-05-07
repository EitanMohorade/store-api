package com.store.api.service;

import com.store.api.dto.configuracionTienda.ConfiguracionTiendaDTO;
import com.store.api.entity.ConfiguracionTienda;
import com.store.api.repository.ConfiguracionTiendaRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio para la configuración global de la tienda.
 *
 * Maneja un registro singleton (ID = 1). Si no existe en la base de datos,
 * lo crea con valores por defecto al primer acceso.
 */
@Service
public class ConfiguracionTiendaService {

    private static final Long SINGLETON_ID = 1L;

    private final ConfiguracionTiendaRepository repository;

    public ConfiguracionTiendaService(ConfiguracionTiendaRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene la configuración actual de la tienda.
     * Si no existe, la crea con valores por defecto.
     *
     * @return ConfiguracionTiendaDTO con los valores actuales
     */
    public ConfiguracionTiendaDTO get() {
        ConfiguracionTienda config = repository.findById(SINGLETON_ID)
                .orElseGet(this::crearConfiguracionPorDefecto);
        return toDTO(config);
    }

    /**
     * Actualiza la configuración de la tienda.
     * Si no existe, la crea con los valores enviados.
     *
     * @param dto ConfiguracionTiendaDTO con los nuevos valores
     * @return ConfiguracionTiendaDTO actualizado
     */
    public ConfiguracionTiendaDTO update(ConfiguracionTiendaDTO dto) {
        ConfiguracionTienda config = repository.findById(SINGLETON_ID)
                .orElseGet(this::crearConfiguracionPorDefecto);

        config.setNombre(dto.getNombre());
        config.setDireccion(dto.getDireccion());
        config.setTagline(dto.getTagline());
        config.setDescripcion(dto.getDescripcion());

        return toDTO(repository.save(config));
    }

    /**
     * Crea y persiste una configuración con valores por defecto.
     */
    private ConfiguracionTienda crearConfiguracionPorDefecto() {
        ConfiguracionTienda config = new ConfiguracionTienda(
                "Mi Tienda",
                null,
                null,
                null
        );
        return repository.save(config);
    }

    private ConfiguracionTiendaDTO toDTO(ConfiguracionTienda config) {
        return new ConfiguracionTiendaDTO(
                config.getNombre(),
                config.getDireccion(),
                config.getTagline(),
                config.getDescripcion()
        );
    }
}
