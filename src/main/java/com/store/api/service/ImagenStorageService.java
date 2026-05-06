package com.store.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.store.api.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestión de imágenes usando Cloudinary.
 *
 * Maneja la subida y eliminación de imágenes de productos en Cloudinary.
 * Las imágenes se almacenan en la carpeta "productos" del bucket.
 */
@Service
public class ImagenStorageService {

    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "image/jpeg", "image/png", "image/webp"
    );

    private static final long MAX_TAMANIO_BYTES = 5 * 1024 * 1024; // 5MB

    private final Cloudinary cloudinary;

    public ImagenStorageService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    /**
     * Sube una imagen a Cloudinary y retorna la URL pública y el public_id.
     *
     * @param archivo Archivo de imagen a subir
     * @return Map con "url" y "publicId" de la imagen subida
     * @throws ValidationException si el archivo no es válido
     * @throws RuntimeException si ocurre un error al subir
     */
    public Map<String, String> guardar(MultipartFile archivo) {
        validarArchivo(archivo);

        try {
            Map resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap("folder", "productos")
            );

            return Map.of(
                    "url", (String) resultado.get("secure_url"),
                    "publicId", (String) resultado.get("public_id")
            );

        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }

    /**
     * Elimina una imagen de Cloudinary usando su public_id.
     *
     * @param publicId Public ID de la imagen en Cloudinary
     */
    public void eliminar(String publicId) {
        if (publicId == null || publicId.isBlank()) return;

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen de Cloudinary", e);
        }
    }

    /**
     * Valida que el archivo sea una imagen permitida y no supere el tamaño máximo.
     *
     * @param archivo Archivo a validar
     * @throws ValidationException si el archivo no cumple las validaciones
     */
    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ValidationException("El archivo de imagen no puede estar vacío");
        }
        if (!TIPOS_PERMITIDOS.contains(archivo.getContentType())) {
            throw new ValidationException("Solo se permiten imágenes en formato JPG, PNG o WEBP");
        }
        if (archivo.getSize() > MAX_TAMANIO_BYTES) {
            throw new ValidationException("La imagen no puede superar los 5MB");
        }
    }
}
