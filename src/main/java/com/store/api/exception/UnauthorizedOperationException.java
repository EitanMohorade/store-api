package com.store.api.exception;

/**
 * Operación no autorizada según reglas de negocio (no auth).
 */
public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException() {
        super(ErrorCode.UNAUTHORIZED_OPERATION.defaultMessage());
    }
    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
