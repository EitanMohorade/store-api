package com.store.api.exception;

/**
 * Operación prohibida por reglas de negocio.
 */
public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException() {
        super(ErrorCode.OPERATION_NOT_ALLOWED.defaultMessage());
    }
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
