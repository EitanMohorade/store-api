package com.store.api.exception;

/**
 * Error de validación específico de la aplicación.
 */
public class ValidationException extends RuntimeException {
    public ValidationException() {
        super(ErrorCode.VALIDATION_ERROR.defaultMessage());
    }
    public ValidationException(String message) {
        super(message);
    }
}
