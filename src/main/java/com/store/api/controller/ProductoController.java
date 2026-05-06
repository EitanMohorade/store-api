package com.store.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

import com.store.api.dto.producto.ProductoCreateDTO;
import com.store.api.dto.producto.ProductoResponseDTO;
import com.store.api.dto.producto.ProductoUpdateDTO;
import com.store.api.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> list() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    /**
     * Crea un producto recibiendo los datos como JSON en la parte "datos"
     * y la imagen como archivo en la parte "imagen" (opcional).
     *
     * Ejemplo con curl:
     *   curl -X POST /api/productos \
     *     -F "datos={\"articulo\":\"ABC\",\"precio\":100,...};type=application/json" \
     *     -F "imagen=@foto.jpg"
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponseDTO> create(
            @RequestPart("datos") @Valid ProductoCreateDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        ProductoResponseDTO creado = productoService.create(dto, imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Actualiza un producto recibiendo los datos como JSON en la parte "datos"
     * y opcionalmente una nueva imagen en la parte "imagen".
     * Si no se envía imagen, se conserva la imagen existente.
     *
     * Ejemplo con curl:
     *   curl -X PUT /api/productos/1 \
     *     -F "datos={\"articulo\":\"ABC\",\"precio\":200,...};type=application/json" \
     *     -F "imagen=@nueva_foto.jpg"
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("datos") @Valid ProductoUpdateDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        return ResponseEntity.ok(productoService.update(id, dto, imagen));
    }

    /**
     * Elimina un producto y su imagen asociada en Cloudinary.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
