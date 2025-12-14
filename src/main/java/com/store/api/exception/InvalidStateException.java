package com.store.api.exception;

/**
 * Estado de entidad/operación inválido para la acción solicitada.
 */
public class InvalidStateException extends RuntimeException {
    public InvalidStateException() {
        super(ErrorCode.INVALID_STATE.defaultMessage());
    }
    public InvalidStateException(String message) {
        super(message);
    }
}

