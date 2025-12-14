package com.store.api.exception;

/**
 * Inconsistencia de datos de la aplicación
 * (relaciones inválidas o reglas de negocio rotas).
 */
public class DataIntegrityException extends RuntimeException {
    public DataIntegrityException() {
        super(ErrorCode.DATA_INTEGRITY.defaultMessage());
    }
    public DataIntegrityException(String message) {
        super(message);
    }
}
