package com.store.api.exception;

/**
 * Códigos de error de la aplicación con mensajes por defecto.
 */
public enum ErrorCode {
    RESOURCE_NOT_FOUND("Recurso no encontrado"),
    BAD_REQUEST("Solicitud inválida"),
    VALIDATION_ERROR("Error de validación de datos"),
    INVALID_STATE("Estado inválido para la operación"),
    DUPLICATE_RESOURCE("El recurso ya existe"),
    STOCK_INSUFFICIENT("Stock insuficiente para la operación"),
    OPERATION_NOT_ALLOWED("Operación no permitida"),
    UNAUTHORIZED_OPERATION("Operación no autorizada"),
    DATA_INTEGRITY("Inconsistencia de datos detectada");

    private final String defaultMessage;

    ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
